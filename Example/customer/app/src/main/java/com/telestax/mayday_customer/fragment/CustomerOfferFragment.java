
/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.telestax.mayday_customer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.telestax.mayday_customer.R;
import com.telestax.mayday_customer.activity.CustomerMainActivity;
import com.telestax.mayday_customer.utils.CustomerConstant;

public class CustomerOfferFragment extends Fragment implements View.OnClickListener {

    private CustomerOfferInterface mCallBack;
    private BroadcastReceiver mInitReceiver;
    private ImageView mImageViewCustomerMayDayCall;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBack = (CustomerOfferInterface) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewInfo = inflater.inflate(R.layout.customer_offer, container, false);

        // Initialize UI
        mImageViewCustomerMayDayCall = (ImageView) viewInfo.findViewById(R.id.imageView_customer_three_mayday);
        ImageView imageViewCustomerOfferBack = (ImageView) viewInfo.findViewById(R.id.imageView_customer_offer_back);
        mImageViewCustomerMayDayCall.setOnClickListener(this);
        imageViewCustomerOfferBack.setOnClickListener(this);

        String mayDayAction = CustomerMainActivity.getMaydaySharePref(getActivity());
        if (mayDayAction != null) {
            if (mayDayAction.equalsIgnoreCase(CustomerConstant.YES)) {
                mImageViewCustomerMayDayCall.setVisibility(View.INVISIBLE);
            } else {
                mImageViewCustomerMayDayCall.setVisibility(View.VISIBLE);
            }
        }

        mInitReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                mImageViewCustomerMayDayCall.setVisibility(View.VISIBLE);
            }
        };

        //Register broadcast receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mInitReceiver, new IntentFilter(CustomerConstant.BROADCAST_INTENT));


        return viewInfo;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageView_customer_three_mayday) {
            final CharSequence[] items = {
                    getResources().getString(R.string.video), getResources().getString(R.string.instant_message),
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.action));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (item == 0) {
                        mCallBack.onVideoCall();
                        mImageViewCustomerMayDayCall.setVisibility(View.INVISIBLE);
                    } else if (item == 1) {
                        mCallBack.onChatMessage();
                        mImageViewCustomerMayDayCall.setVisibility(View.INVISIBLE);
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (v.getId() == R.id.imageView_customer_offer_back) {
            mCallBack.onBackArrow();
        }
    }

    public interface CustomerOfferInterface {
        void onVideoCall();

        void onChatMessage();

        void onBackArrow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //UnRegister broadcast receiver
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mInitReceiver);

    }
}
