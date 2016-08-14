package com.cloudhome.mobilesafer.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.db.BlackNumberDao;
import com.cloudhome.mobilesafer.domain.BlackNumberInfo;
import java.util.List;

/**
 * Created by xionghu on 2016/7/14.
 * Email：965705418@qq.com
 */
public class CallSmsSafeAdapter extends BaseAdapter {

    private List<BlackNumberInfo> infos;

    private Context context;
    private LayoutInflater layoutInflater;



    public CallSmsSafeAdapter(Context context) {
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<BlackNumberInfo> infos) {
        this.infos = infos;

    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int i) {
        return infos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.call_sms_safe_item, parent, false);
            holder.tv_number   = (TextView)  convertView.findViewById(R.id.tv_number);
            holder.tv_mode     = (TextView)  convertView.findViewById(R.id.tv_mode);
            holder.iv_delete   = (ImageView) convertView.findViewById(R.id.iv_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //得到黑名单数据-根据位置去得到
        final BlackNumberInfo info = infos.get(position);

        holder.tv_number.setText(info.getNumber());


        String mode = info.getMode();
        if("0".equals(mode))
        {


            //电话拦截

            holder.tv_mode.setText("电话拦截+短信拦截");

        }else if("1".equals(mode)){

            //短信拦截

            holder.tv_mode.setText("短信拦截");

        }else{

            holder.tv_mode.setText("全部拦截");

        }


        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              System.out.print("position=="+position);
                BlackNumberDao dao = new BlackNumberDao(context);
              //1.把数据库中的数据删除
                dao.delete(info.getNumber());
              //2.把当前列表数据删除
                infos.remove(info);
              //3.刷新数据
                CallSmsSafeAdapter.this.notifyDataSetChanged();

            }
        });


        return convertView;
    }

    static class ViewHolder {

        TextView  tv_number;
        TextView  tv_mode;
        ImageView iv_delete;
    }

}