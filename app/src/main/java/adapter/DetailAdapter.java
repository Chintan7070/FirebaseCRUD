package adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myditail.R;
import com.example.myditail.ReadData;
import com.example.myditail.UpatePage;
import com.example.myditail.modelclass.FireModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewData> {

    private final List<FireModel> list;
    Activity activity;

    public DetailAdapter(ReadData readData, List<FireModel> list) {
        activity = readData;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.my_list,parent,false);
        return new ViewData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewData holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.surname.setText(list.get(position).getSurname());
        holder.gender.setText(list.get(position).getGender());
        holder.age.setText(list.get(position).getAge());
        holder.contat.setText(list.get(position).getContact());
        holder.email.setText(list.get(position).getEmail());
        holder.contat.setText(list.get(position).getContact());
        holder.password.setText(list.get(position).getPassword());
        Glide.with(activity).load(list.get(position).getLink()).placeholder(R.drawable.default_img).into(holder.myimage);
        String key1 = list.get(position).getKey();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dbr = firebaseDatabase.getReference();
                dbr.child("MyDetail").child(key1).removeValue();
                Toast.makeText(activity,"Record Deleted",Toast.LENGTH_SHORT).show();
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UpatePage.class);
                intent.putExtra("key",key1);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewData extends RecyclerView.ViewHolder{
        private final TextView name,surname,gender,age,contat,email,password;
        private final Button delete,update;
        private final ImageView myimage;

        public ViewData(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.rname);
            surname = itemView.findViewById(R.id.rsurname);
            gender = itemView.findViewById(R.id.rgender);
            age = itemView.findViewById(R.id.rage);
            contat = itemView.findViewById(R.id.rcontact);
            email = itemView.findViewById(R.id.remail);
            password = itemView.findViewById(R.id.rpassword);
            myimage=itemView.findViewById(R.id.myimage);
            update=itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);


        }
    }
}
