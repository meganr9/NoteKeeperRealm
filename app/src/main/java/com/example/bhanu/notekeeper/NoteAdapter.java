package com.example.bhanu.notekeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Create by Megan Reiffer
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.CustomViewHolder>  {
    RealmResults<Note> mAE;
    Context context;
    private io.realm.Realm realm;

    public NoteAdapter(Context context, RealmResults<Note> mAE) {
        this.context = context;
        this.mAE = mAE;

// Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView Title;
        public TextView Priority;
        public TextView Time;
        public CheckBox c;
        public RelativeLayout rowLayout;

        public CustomViewHolder(View v) {
            super(v);
      this.Title=(TextView)v.findViewById(R.id.tv1);
            this.Priority=(TextView)v.findViewById(R.id.tv2);
            this.Time=(TextView)v.findViewById(R.id.tv3);
            this.c=(CheckBox) v.findViewById(R.id.checkBox);
            this.rowLayout = (RelativeLayout) v.findViewById(R.id.rowLayout);

        }
    }
    @Override
    public NoteAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, null);
        CustomViewHolder CV=new CustomViewHolder(view);
        return CV;

    }

    @Override
    public void onBindViewHolder(final NoteAdapter.CustomViewHolder holder, int position) {
       final Note note=mAE.get(position);
        holder.Title.setText(note.getSubject());
        holder.Priority.setText(note.getPriority());
        PrettyTime prettyTime = new PrettyTime();
        holder.Time.setText(prettyTime.format(mAE.get(position).getUpDateTime()));
        if (note.getStatus().equals("completed"))
        {
            holder.c.setChecked(true);
        }

        final String noteID = note.getId();
        holder.c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.getStatus().equals("pending")) {
                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure that you want to mark it as completed?")
                            .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.c.setChecked(false);
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
// Asynchronously update objects on a background thread
                                    holder.c.setChecked(true);
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm bgRealm) {
                                            Note curNote = bgRealm.where(Note.class).equalTo("id", noteID).findFirst();
                                            curNote.setStatus("completed");

                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            // Original queries and Realm objects are automatically updated.
                                            Log.d(TAG, "onSuccess: Succesfully set to completed");
                                        }
                                    });


                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure that you want to mark it to  pending?")
                            .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.c.setChecked(true);
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
// Asynchronously update objects on a background thread

                                    holder.c.setChecked(false);
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm bgRealm) {

                                            Note curNote = bgRealm.where(Note.class).equalTo("id", noteID).findFirst();
                                            curNote.setStatus("pending");
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            // Original queries and Realm objects are automatically updated.
                                            Log.d(TAG, "onSuccess: Succesfully set to pending");
                                        }
                                    });

                                }
                            })
                            .show();
                }
            }
        });

        holder.rowLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure that you want to delete this?")
                        .setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// Asynchronously update objects on a background thread

                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm bgRealm) {

                                        Note curNote = bgRealm.where(Note.class).equalTo("id", noteID).findFirst();
                                        curNote.deleteFromRealm();
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        // Original queries and Realm objects are automatically updated.
                                        Log.d(TAG, "onSuccess: deleted!");
                                    }
                                });

                            }
                        })
                        .show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAE.size();
    }
}
