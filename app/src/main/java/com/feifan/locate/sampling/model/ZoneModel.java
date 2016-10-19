package com.feifan.locate.sampling.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.feifan.locate.provider.LocateData;
import com.feifan.locate.widget.cursorwork.CursorModel;

/**
 * 采样区域Model
 * Created by xuchunlei on 16/9/5.
 */
public class ZoneModel extends CursorModel implements Parcelable {
    public String name;
    public String plan;
    public float scale;
    public int floorNo;

    public ZoneModel(Cursor cursor) {
        super(cursor);
        int nameIndex = cursor.getColumnIndexOrThrow(LocateData.Zone.NAME);
        name = cursor.getString(nameIndex);
        int planIndex = cursor.getColumnIndexOrThrow(LocateData.Zone.PLAN);
        plan = cursor.getString(planIndex);
        int scaleIndex = cursor.getColumnIndexOrThrow(LocateData.Zone.SCALE);
        scale = cursor.getFloat(scaleIndex);
        int floorNoIndex = cursor.getColumnIndexOrThrow(LocateData.Zone.FLOOR_NO);
        floorNo = cursor.getInt(floorNoIndex);
    }

    protected ZoneModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        plan = in.readString();
        scale = in.readFloat();
        floorNo = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(plan);
        dest.writeFloat(scale);
        dest.writeInt(floorNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ZoneModel> CREATOR = new Parcelable.Creator<ZoneModel>() {
        @Override
        public ZoneModel createFromParcel(Parcel in) {
            return new ZoneModel(in);
        }

        @Override
        public ZoneModel[] newArray(int size) {
            return new ZoneModel[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }
}
