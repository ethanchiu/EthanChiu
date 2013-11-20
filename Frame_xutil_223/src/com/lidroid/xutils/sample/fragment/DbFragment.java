package com.lidroid.xutils.sample.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.sample.R;
import com.lidroid.xutils.sample.entities.Child;
import com.lidroid.xutils.sample.entities.Parent;
import com.lidroid.xutils.sample.entities.TestEntity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author: wyouflf
 * Date: 13-9-14
 * Time: 下午3:35
 */
public class DbFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.db_fragment, container, false);
        ViewUtils.inject(this, view);
        
        database = DbUtils.create(this.getActivity());
        database.configAllowTransaction(true);
        database.configDebug(true);
        
        return view;
    }

    @ViewInject(R.id.db_test_btn)
    private Button stopBtn;
    @ViewInject(R.id.add_btn)
    private Button addBtn;
    @ViewInject(R.id.delete_btn)
    private Button deleteBtn;
    @ViewInject(R.id.update_btn)
    private Button updateBtn;
    @ViewInject(R.id.selectBtn)
    private Button selectBtn;
    @ViewInject(R.id.dropDbBtn)
    private Button dropDbBtn;

    @ViewInject(R.id.result_txt)
    private TextView resultText;

    
    TestEntity entity;
    
    @OnClick(R.id.add_btn)
    public void addBtnClick(View v){
    	try {
    		entity = new TestEntity();
    		entity.setId("1");
    		entity.setName("ethan");
    		entity.setAge(10);
    		
			database.save(entity);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @OnClick(R.id.delete_btn)
    public void delete(View v){
    	
    	try {
			database.delete(entity);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    @OnClick(R.id.update_btn)
    public void update(View v){
    	entity.setAge(1);
    	entity.setName("ethan2");
    	try {
			database.update(entity, "name");
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @OnClick(R.id.selectBtn)
    public void select(View v){
    	try {
			TestEntity entityx = database.findFirst(entity);
			resultText.setText(entityx.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @OnClick(R.id.dropDbBtn)
    public void dropdb(View v){
    	try {
			database.dropTable(TestEntity.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    DbUtils database;
    @OnClick(R.id.db_test_btn)
    public void testDb(View view) {

        String temp = "";

        Parent parent = new Parent();
        parent.name = "测试";
        parent.setAdmin(true);
        parent.setEmail("wyouflf@gmail.com");

        /*Parent parent2 = new Parent();
        parent2.name = "测试2";
        parent2.isVIP = false;*/

        try {

            //DbUtils db = DbUtils.create(this.getActivity(), "/sdcard/", "test");

            Child child = new Child();
            child.name = "child' name";
            //db.saveBindingId(parent);
            //child.parent = new ForeignLazyLoader<Parent>(Child.class, "parentId", parent.getId());
            //child.parent = parent;

            Parent test = database.findFirst(parent); // 通过parent的属性查找
            //Parent test = db.findFirst(Selector.from(Parent.class).where("id", "in", new int[]{1, 3, 6}));
            //Parent test = db.findFirst(Selector.from(Parent.class).where("id", "between", new String[]{"1", "5"}));
            if (test != null) {
                child.parent = test;
                temp += "first parent:" + test + "\n";
                resultText.setText(temp);
            } else {
                child.parent = parent;
            }

            parent.setTime(new Date());
            parent.setDate(new java.sql.Date(new Date().getTime()));

            database.saveBindingId(child);//保存对象关联数据库生成的id

            List<Child> children = database.findAll(Selector.from(Child.class));//.where(WhereBuilder.b("name", "=", "child' name")));
            temp += "children size:" + children.size() + "\n";
            resultText.setText(temp);
            if (children.size() > 0) {
                temp += "last children:" + children.get(children.size() - 1) + "\n";
                resultText.setText(temp);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            calendar.add(Calendar.HOUR, 3);

            List<Parent> list = database.findAll(
                    Selector.from(Parent.class)
                            .where("id", "<", 54)
                            .and("time", ">", calendar.getTime())
                            .orderBy("id")
                            .limit(10));
            temp += "find parent size:" + list.size() + "\n";
            resultText.setText(temp);
            if (list.size() > 0) {
                temp += "last parent:" + list.get(list.size() - 1) + "\n";
                resultText.setText(temp);
            }

            //parent.name = "hahaha123";
            //db.update(parent);

            Parent entity = database.findById(Parent.class, child.parent.getId());
            temp += "find by id:" + entity.toString() + "\n";
            resultText.setText(temp);

            List<DbModel> dbModels = database.findDbModelAll(Selector.from(Parent.class)
                    .groupBy("name")
                    .select("name", "count(name) as count"));
            temp += "group by result:" + dbModels.get(0).getDataMap() + "\n";
            resultText.setText(temp);

        } catch (DbException e) {
            temp += "error :" + e.getMessage() + "\n";
            resultText.setText(temp);
        }
    }
}
