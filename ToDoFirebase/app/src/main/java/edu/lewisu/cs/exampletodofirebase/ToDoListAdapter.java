package edu.lewisu.cs.exampletodofirebase;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ToDoListAdapter {
    class ToDoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView titleTextView;

        ToDoHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick(adapterPosition);
        }
    }




    private final TodoListAdapterOnClickHandler clickHandler=null;


    public interface TodoListAdapterOnClickHandler {
        void onClick(int position);
    }


}


