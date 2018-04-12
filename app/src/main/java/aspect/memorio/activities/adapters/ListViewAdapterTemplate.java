package aspect.memorio.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.util.List;

import aspect.memorio.R;
import aspect.memorio.fragments.ListFragment;
import aspect.memorio.models.Priority;
import aspect.memorio.utils.Utils;

public abstract class ListViewAdapterTemplate<T> extends ArrayAdapter<T> {

    private static final float MIN_OPACITY = 0.3f;

    private final ListFragment<T> fragment;
    private final ListViewAdapterItemConfig config;

    public ListViewAdapterTemplate(Context context, List<T> items, ListFragment<T> fragment, ListViewAdapterItemConfig config) {
        super(context, config.layout, items);
        this.config  = config;
        this.fragment = fragment;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // TODO: pass this parent maybe?
        final View view = this.createView(convertView);
        final T item = getItem(position);

        if (item != null) {
            this.setTextView(view);
            this.setAdditionalTextView(view);
            this.setRemoveButtonAction(view, item);
            this.setItemClickListener(view, item);
            this.setBackgroundColor(view, item);
            this.setOpacity(view, item);
            this.setAdditional(view, item);
        }

        return view;
    }

    protected abstract String getItemText();

    protected abstract String getItemAdditionalText();

    protected abstract int getItemBackgroundColorResource(T item);

    protected abstract float getTextOpacity(T item);

    protected void setAdditional(View itemView, T item) {

    }

    private View createView(View convertView) {
        if (convertView != null) {
            return convertView;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        return inflater.inflate(this.config.layout, null);
    }

    private void setTextView(View itemView) {
        TextView textView = itemView.findViewById(this.config.text);
        textView.setText(this.getItemText().isEmpty()? this.fragment.getString(R.string.empty_string) : this.getItemText());
    }

    private void setAdditionalTextView(View itemView) {
        TextView additionalTextView = itemView.findViewById(this.config.additionalText);
        additionalTextView.setText(this.getItemAdditionalText().isEmpty()?
                this.fragment.getText(R.string.empty_string): this.getItemAdditionalText());

    }

    private void setRemoveButtonAction(View view, final T item) {
        Button removeButton = view.findViewById(this.config.removeButton);
        removeButton.setAlpha(0.5f);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAlpha(1.0f);
                fragment.removeItem(item);
            }
        });
    }

    private void setItemClickListener(View itemView, final T item) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.editItem(item);
            }
        });
    }

    private void setBackgroundColor(View itemView, T item) {
        itemView.setBackgroundColor(getContext().getResources().getColor(this.getItemBackgroundColorResource(item)));
    }

    private void setOpacity(View view, T item) {
        float opacity = this.getTextOpacity(item);
        view.findViewById(R.id.note_text).setAlpha(opacity);
        view.findViewById(R.id.note_remaining_time).setAlpha(opacity);
    }

    protected int getPriorityColor(Priority priority) {
        switch (priority) {
            case HIGH:
                return R.color.high_priority_item;
            case MEDIUM:
                return R.color.medium_priority_item;
            case LOW:
                return R.color.low_priority_item;
            default:
                return R.color.white;
        }
    }

    protected float getDateOpacity(Date date) {
        long minutesDiff = Utils.getTimeRemainingFromNowInMinutes(date);
        if (minutesDiff <= 0) {
            return 1.0f;
        }

        float monthsRemaining = minutesDiff / (60 * 24 * 30.0f);
        if (monthsRemaining > 1.5) {
            return MIN_OPACITY;
        } else if (monthsRemaining < 0.3f) {
            return 1.0f;
        } else {
            return (1 - monthsRemaining) * MIN_OPACITY + MIN_OPACITY;
        }
    }

}
