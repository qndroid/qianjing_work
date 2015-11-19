package com.qianjing.finance.ui.activity.fund;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qianjing.finance.model.common.Card;
import com.qianjing.finance.ui.activity.common.BaseActivity;
import com.qianjing.finance.util.Flag;
import com.qianjing.finance.widget.adapter.fundtype.CardListAdapter;
import com.qjautofinancial.R;

/**********************************************************
 * @文件名称：CardListActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月10日 下午4:48:46
 * @文件描述：银行卡列表页面
 * @修改历史：2015年6月10日创建初始版本
 **********************************************************/
public class CardListActivity extends BaseActivity implements OnItemClickListener
{
	private ListView cardListView;
	private ArrayList<Card> listData;
	private Card selectedCard;
	private CardListAdapter adapter;
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}

	private void initData()
	{
		mIntent = getIntent();
		listData = mIntent.getParcelableArrayListExtra(Flag.EXTRA_BEAN_CARD_LIST);
		selectedCard = mIntent.getParcelableExtra(Flag.EXTRA_BEAN_CARD_CURRENT);
	}

	private void initView()
	{
		setContentView(R.layout.activity_card_list_view);

		setTitleWithId(R.string.xuan_zhe_ying_hang_ka);
		setTitleBack();

		cardListView = (ListView) findViewById(R.id.card_list_view);
		cardListView.setOnItemClickListener(this);
		adapter = new CardListAdapter(this, listData, selectedCard);
		cardListView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Card card = (Card) adapter.getItem(position);
		mIntent.putExtra(Flag.EXTRA_BEAN_CARD_COMMON, card);
		setResult(RESULT_OK, mIntent);
		finish();
	}

}