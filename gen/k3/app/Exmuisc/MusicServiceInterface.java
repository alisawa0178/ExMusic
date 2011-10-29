/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\aokai\\workspace2\\ExMusic\\src\\k3\\app\\Exmuisc\\MusicServiceInterface.aidl
 */
package k3.app.Exmuisc;
public interface MusicServiceInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements k3.app.Exmuisc.MusicServiceInterface
{
private static final java.lang.String DESCRIPTOR = "k3.app.Exmuisc.MusicServiceInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an k3.app.Exmuisc.MusicServiceInterface interface,
 * generating a proxy if needed.
 */
public static k3.app.Exmuisc.MusicServiceInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof k3.app.Exmuisc.MusicServiceInterface))) {
return ((k3.app.Exmuisc.MusicServiceInterface)iin);
}
return new k3.app.Exmuisc.MusicServiceInterface.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setList:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<k3.app.Exmuisc.MusicData> _arg0;
_arg0 = data.createTypedArrayList(k3.app.Exmuisc.MusicData.CREATOR);
this.setList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setListNum:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<k3.app.Exmuisc.MusicData> _arg0;
_arg0 = data.createTypedArrayList(k3.app.Exmuisc.MusicData.CREATOR);
int _arg1;
_arg1 = data.readInt();
this.setListNum(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getList:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<k3.app.Exmuisc.MusicData> _result = this.getList();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_Musicstart:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.Musicstart();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_pause:
{
data.enforceInterface(DESCRIPTOR);
this.pause();
reply.writeNoException();
return true;
}
case TRANSACTION_getTime:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getTime();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_next:
{
data.enforceInterface(DESCRIPTOR);
this.next();
reply.writeNoException();
return true;
}
case TRANSACTION_back:
{
data.enforceInterface(DESCRIPTOR);
this.back();
reply.writeNoException();
return true;
}
case TRANSACTION_getData:
{
data.enforceInterface(DESCRIPTOR);
k3.app.Exmuisc.MusicData _result = this.getData();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getFinishTime:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getFinishTime();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_seekTo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.seekTo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isPaly:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isPaly();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_changeMusic:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.changeMusic();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_onLoop:
{
data.enforceInterface(DESCRIPTOR);
this.onLoop();
reply.writeNoException();
return true;
}
case TRANSACTION_getLoop:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getLoop();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getOneLoop:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getOneLoop();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_onRandom:
{
data.enforceInterface(DESCRIPTOR);
this.onRandom();
reply.writeNoException();
return true;
}
case TRANSACTION_getRandom:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getRandom();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_onAB:
{
data.enforceInterface(DESCRIPTOR);
this.onAB();
reply.writeNoException();
return true;
}
case TRANSACTION_getAB:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAB();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements k3.app.Exmuisc.MusicServiceInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
//リストの登録

public void setList(java.util.List<k3.app.Exmuisc.MusicData> data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(data);
mRemote.transact(Stub.TRANSACTION_setList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//リストの登録(開始指定)

public void setListNum(java.util.List<k3.app.Exmuisc.MusicData> data, int i) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(data);
_data.writeInt(i);
mRemote.transact(Stub.TRANSACTION_setListNum, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//リストの取得

public java.util.List<k3.app.Exmuisc.MusicData> getList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<k3.app.Exmuisc.MusicData> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getList, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(k3.app.Exmuisc.MusicData.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//指定された音楽ファイルを再生する

public int Musicstart() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_Musicstart, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//一時停止する

public void pause() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pause, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//現在の時間

public int getTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void next() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_next, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void back() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_back, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//現在の曲を取得

public k3.app.Exmuisc.MusicData getData() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
k3.app.Exmuisc.MusicData _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getData, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = k3.app.Exmuisc.MusicData.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//現在の終了時刻を取得

public int getFinishTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getFinishTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//再生位置移動

public void seekTo(int i) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(i);
mRemote.transact(Stub.TRANSACTION_seekTo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//再生中か？

public boolean isPaly() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isPaly, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//曲が変わったか？（曲情報の更新用に）

public boolean changeMusic() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_changeMusic, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//

public void onLoop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onLoop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public boolean getLoop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLoop, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean getOneLoop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getOneLoop, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void onRandom() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onRandom, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public boolean getRandom() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRandom, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void onAB() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onAB, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int getAB() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAB, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setListNum = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_Musicstart = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_pause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_next = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_back = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getFinishTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_seekTo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_isPaly = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_changeMusic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_onLoop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_getLoop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getOneLoop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_onRandom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_getRandom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_onAB = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_getAB = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
}
//リストの登録

public void setList(java.util.List<k3.app.Exmuisc.MusicData> data) throws android.os.RemoteException;
//リストの登録(開始指定)

public void setListNum(java.util.List<k3.app.Exmuisc.MusicData> data, int i) throws android.os.RemoteException;
//リストの取得

public java.util.List<k3.app.Exmuisc.MusicData> getList() throws android.os.RemoteException;
//指定された音楽ファイルを再生する

public int Musicstart() throws android.os.RemoteException;
//一時停止する

public void pause() throws android.os.RemoteException;
//現在の時間

public int getTime() throws android.os.RemoteException;
public void next() throws android.os.RemoteException;
public void back() throws android.os.RemoteException;
//現在の曲を取得

public k3.app.Exmuisc.MusicData getData() throws android.os.RemoteException;
//現在の終了時刻を取得

public int getFinishTime() throws android.os.RemoteException;
//再生位置移動

public void seekTo(int i) throws android.os.RemoteException;
//再生中か？

public boolean isPaly() throws android.os.RemoteException;
//曲が変わったか？（曲情報の更新用に）

public boolean changeMusic() throws android.os.RemoteException;
//

public void onLoop() throws android.os.RemoteException;
public boolean getLoop() throws android.os.RemoteException;
public boolean getOneLoop() throws android.os.RemoteException;
public void onRandom() throws android.os.RemoteException;
public boolean getRandom() throws android.os.RemoteException;
public void onAB() throws android.os.RemoteException;
public int getAB() throws android.os.RemoteException;
}
