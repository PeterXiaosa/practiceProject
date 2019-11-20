package com.peter.practiceproject.studyitem.ipc;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Book implements Parcelable {
    // 库存量
    private int count;
    // 订购量
    private int buy;

    private String name;

    private double price;

    public Book() {
    }

    protected Book(Parcel in) {
        count = in.readInt();
        buy = in.readInt();
        name = in.readString();
        price = in.readDouble();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeInt(buy);
        dest.writeString(name);
        dest.writeDouble(price);
    }

    @NonNull
    @Override
    public String toString() {
        return "book count : " + count + ", buy : " + buy + ", name : " + name + ", price : " + price ;
    }
}
