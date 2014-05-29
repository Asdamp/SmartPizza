package com.asdamp.smartpizza;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingrediente implements Parcelable{
	public String nome;
	public boolean checked;
	public Ingrediente(String categoria){
		this.nome=categoria;
		checked=true;
	}
	public Ingrediente(String categoria,boolean c){
		this.nome=categoria;
		checked=c;
	}
	public Ingrediente(Parcel in) {
		 nome=in.readString();
		checked=in.readByte()==1;
	  
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nome);
		dest.writeByte((byte) (checked ? 1 : 0)); 		
	}
	 public static final Parcelable.Creator<Ingrediente> CREATOR
     = new Parcelable.Creator<Ingrediente>() {
 public Ingrediente createFromParcel(Parcel in) {
	 return new Ingrediente(in);
 }

 public Ingrediente[] newArray(int size) {
     return new Ingrediente[size];
 }
};
}
