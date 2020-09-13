package ikbal.mulalic.reflektor;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ikbal.mulalic.reflektor.model.Report;

public class RecyclerViewMyReportsAdapter extends RecyclerView.Adapter<RecyclerViewMyReportsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Report> listOfReports = new ArrayList<>();
    private Report myReport;

    public RecyclerViewMyReportsAdapter(Context context, ArrayList<Report> listOfReports)
    {
        this.context = context;
        this.listOfReports = listOfReports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_moje_prijave,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                //System.out.println("Izo " + myReport.getCategoryOfReport());
                holder.dateOfReportTextView.setText(listOfReports.get(position).getDescriptionOfReport());
                //holder.categoryOfReportTextView.setText(myReport.getCategoryOfReport());
                //holder.statusOfReportTextView.setText(myReport.getLocationOfReport());



            /* holder.deleteReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Obrisana prijava",Toast.LENGTH_SHORT).show();
                }
            });
            holder.sendReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Poslana prijava",Toast.LENGTH_SHORT).show();
                }
            });*/

    }

    @Override
    public int getItemCount() {
        return listOfReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView dateOfReportTextView,categoryOfReportTextView,statusOfReportTextView;
        ImageButton deleteReportButton,sendReportButton;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            dateOfReportTextView = (TextView)itemView.findViewById(R.id.dateOfReportTextView);
            categoryOfReportTextView = (TextView)itemView.findViewById(R.id.categoryOfReportTextView);
            statusOfReportTextView = (TextView)itemView.findViewById(R.id.statusOfReportTextView);
            deleteReportButton = (ImageButton) itemView.findViewById(R.id.deleteReportButton);
            sendReportButton = (ImageButton)itemView.findViewById(R.id.sendReportButton);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.recyclerViewLinearLayout);
        }
    }
}
