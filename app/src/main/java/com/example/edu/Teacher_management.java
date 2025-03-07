package com.example.edu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teacher_management extends AppCompatActivity {
    public  static  Teacher teacher_info;
    CardView cardView_teacher_complain;
    public static String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_management);

        cardView_teacher_complain=findViewById(R.id.teacher_complain_cardview);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(Teacher_management.this);
        userid=pref.getString("userid","000");

        Toast.makeText(getApplicationContext(),userid,Toast.LENGTH_LONG).show();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("teacher_info").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                teacher_info=dataSnapshot.getValue(Teacher.class);
                set_value();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        cardView_teacher_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                teacher_complain(teacher_info.getUser_name());
            }
        });


    }
    static class Teacher
    {
        private String user_name;
        private String batch;
        private String email;
        private String contact_no;
        private String actual_day;
        private String extra_day;
        private String present;
        private String type;


        public Teacher() {}
        public Teacher(String name,String email,String batch,String contact_no,String actual_day,String extra_day,String present,String type)
        {
            this.user_name=name;
            this.batch=batch;
            this.email=email;
            this.contact_no=contact_no;
            this.actual_day=actual_day;
            this.extra_day=extra_day;
            this.present=present;
            this.type=type;
        }


        public String getUser_name() {
            return user_name;
        }

        public String getEmail() {
            return email;
        }

        public String getContact_no() {
            return contact_no;
        }

        public String getActual_day() {
            return actual_day;
        }

        public String getExtra_day() {
            return extra_day;
        }

        public String getBatch() {
            return batch;
        }

        public String getPresent() {
            return present;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setContact_no(String contact_no) {
            this.contact_no = contact_no;
        }

        public void setActual_day(String actual_day) {
            this.actual_day = actual_day;
        }

        public void setExtra_day(String extra_day) {
            this.extra_day = extra_day;
        }

        public void setPresent(String present) {
            this.present = present;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
    public void set_value()
    {
        TextView user_name=findViewById(R.id.teacher_user_name);
        TextView user_phone=findViewById(R.id.teacher_user_phone);
        TextView user_email=findViewById(R.id.teacher_user_email);
        TextView user_batch=findViewById(R.id.teacher_user_batch);
        TextView user_total=findViewById(R.id.teacher_total);
        TextView user_extra=findViewById(R.id.teacher_extra);
        TextView user_present=findViewById(R.id.teacher_present);
        TextView user_sgm_id=findViewById(R.id.teacher_sgm_id);

        user_name.setText(teacher_info.getUser_name());
        user_email.setText(teacher_info.getEmail());
        user_batch.setText(teacher_info.getBatch());
        user_phone.setText(teacher_info.getContact_no());
        user_total.setText(teacher_info.getActual_day());
        user_extra.setText(teacher_info.getExtra_day());
        user_present.setText(teacher_info.getPresent());
        user_sgm_id.setText(userid);


    }
    public void teacher_complain(String name)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.complain_teacher);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        EditText complain=dialog.findViewById(R.id.teacher_complain_text);
        Button done=dialog.findViewById(R.id.teacher_complain_done);
        Button cancel=dialog.findViewById(R.id.teacher_complain_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        String complain_text=complain.getText().toString();

        complain_text=complain_text+"-by "+name+" (from teacher section)";

        final String finalComplain_text = complain_text;
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference databaseReference_complain_counter=FirebaseDatabase.getInstance().getReference().child("complain").child("counter");


                databaseReference_complain_counter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String counter=dataSnapshot.getValue().toString();
                        int count=dataSnapshot.getValue().hashCode();

                        DatabaseReference databaseReference_complain=FirebaseDatabase.getInstance().getReference().child("complain").child(counter);
                        databaseReference_complain.setValue(finalComplain_text);

                        count++;

                        databaseReference_complain_counter.setValue(count);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }});
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setAttributes(lp);

    }
}
