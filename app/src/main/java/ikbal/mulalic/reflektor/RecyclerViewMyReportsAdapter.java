package ikbal.mulalic.reflektor;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ikbal.mulalic.reflektor.model.Report;

public class RecyclerViewMyReportsAdapter extends RecyclerView.Adapter<RecyclerViewMyReportsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Report> listOfReports;
    private OnItemClick onItemClick;

    public RecyclerViewMyReportsAdapter(Context context, ArrayList<Report> listOfReports,OnItemClick onItemClick)
    {
        this.context = context;
        this.listOfReports = listOfReports;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_moje_prijave,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


                holder.dateOfReportTextView.setText(listOfReports.get(position).getCreatedAt());
                if(listOfReports.get(position).isSent())
                {
                    holder.statusOfReportTextView.setTextColor(Color.parseColor("#00FF00"));
                    holder.statusOfReportTextView.setText("Poslano");
                    holder.sendReportButton.setAlpha(0.2f);
                }
                else
                {
                    holder.statusOfReportTextView.setTextColor(Color.parseColor("#ff0000"));
                    holder.statusOfReportTextView.setText("Nije poslano");
                }
                holder.categoryOfReportTextView.setText(listOfReports.get(position).getCategoryOfReport());
                holder.recordedPhotoImageView.setImageBitmap(BitmapFactory.decodeFile(listOfReports.get(position).getPhotoPathReport()));
                if(listOfReports.get(position).isSent())
                {
                    holder.sendReportButton.setEnabled(false);
                }
                else {
                    holder.sendReportButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClick.sendAgain(position);
                        }
                    });
                }

    }

    @Override
    public int getItemCount() {
        return listOfReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView recordedPhotoImageView;
        TextView dateOfReportTextView,categoryOfReportTextView,statusOfReportTextView;
        ImageButton sendReportButton;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recordedPhotoImageView = (ImageView)itemView.findViewById(R.id.recordedPhotoImageView);
            dateOfReportTextView = (TextView)itemView.findViewById(R.id.dateOfReportTextView);
            categoryOfReportTextView = (TextView)itemView.findViewById(R.id.categoryOfReportTextView);
            statusOfReportTextView = (TextView)itemView.findViewById(R.id.statusOfReportTextView);
            sendReportButton = (ImageButton)itemView.findViewById(R.id.sendReportButton);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.recyclerViewLinearLayout);
        }
    }

    public interface OnItemClick
    {
        public void sendAgain(int position);
    }
}
