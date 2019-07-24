package com.zhengx.dialogfragutil;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * name：com.zhengx.fdialogutil
 * class: describe
 * author: zhengx
 * create_time: 19-6-10
 */
public class InitProvider extends ContentProvider {


    @Override
    public boolean onCreate() {
        DialogFragUtils.init((Application) getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder, @Nullable CancellationSignal cancellationSignal) {
        return super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable Bundle queryArgs, @Nullable CancellationSignal cancellationSignal) {
        return super.query(uri, projection, queryArgs, cancellationSignal);
    }

    @NonNull
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
