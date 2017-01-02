package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by caoyujie on 16/12/28.
 * 操作数据库的管理类
 */

public class DBManager {
    private static final String DB_NAME = "DB_NAME";
    private static DBManager INSTANCE;
    private Context mContext;
    private DaoMaster.DevOpenHelper openHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DBManager(Context mContext) {
        this.mContext = mContext;
        openHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
    }

    public static DBManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DBManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DBManager(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 判断数据库是否存在，如果不存在则创建
     */
    public DaoMaster getDaomaster(){
        if(mDaoMaster == null){
            mDaoMaster = new DaoMaster(getWriteableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 创建daoSession,用于操作数据库
     */
    public DaoSession getDaoSession(){
        if(mDaoSession == null){
            mDaoSession = getDaomaster().newSession();
        }
        return mDaoSession;
    }

    /**
     * 获得可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        }
        SQLiteDatabase database = openHelper.getReadableDatabase();
        return database;
    }

    /**
     * 获得可写据库
     */
    private SQLiteDatabase getWriteableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        }
        SQLiteDatabase database = openHelper.getWritableDatabase();
        return database;
    }

    /*********************************插入数据库******************************/
    /**
     * 插入一条数据,(判断id)没有该数据新增一条,有则更新
     */
    public <T> boolean insertEntity(T entity) {
        DaoMaster daoMaster = getDaomaster();
        DaoSession daoSession = getDaoSession();
        boolean flag = daoSession.insertOrReplace(entity) != -1 ? true : false;
        return flag;
    }

    /**
     * 插入多个对象，并开启新的线程
     * @return
     */
    public <T> boolean insertEntitys(final List<T> entitys){
        boolean flag = false;
        if (null == entitys || entitys.isEmpty()){
            return false;
        }
        try {
            getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : entitys) {
                        getDaoSession().insertOrReplace(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /*********************************更新数据库******************************/
    /**
     * 批量更新数据
     * @param objects
     * @return
     */
    public <T> void updateEntitys(final List<T> objects, Class clss){
        if (null == objects || objects.isEmpty()){
            return;
        }
        try {
            getDaoSession().getDao(clss).updateInTx(new Runnable() {
                @Override
                public void run() {
                    for(T object:objects){
                        getDaoSession().update(object);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以对象形式进行数据修改
     * 其中必须要知道对象的主键ID
     * @return
     */
    public <T> boolean updateEntity(T entity){
        boolean flag = false;
        if (null == entity){
            return false;
        }
        try {
            getDaoSession().update(entity);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    /**************************数据库删除操作***********************/
    /**
     * 删除某个数据库表
     * @param clss
     * @return
     */
    public boolean deleteAll(Class clss){
        boolean flag = false;
        try {
            getDaoSession().deleteAll(clss);
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            return flag;
        }
        return flag;
    }

    /**
     * 删除某个对象
     * @param object
     * @return
     */
    public <T> boolean deleteEntity(T object){
        try {
            getDaoSession().delete(object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 异步批量删除数据
     * @param objects
     * @return
     */
    public <T> boolean deleteEntitys(final List<T> objects, Class clss){
        boolean flag = false;
        if (null == objects || objects.isEmpty()){
            return false;
        }
        try {

            getDaoSession().getDao(clss).deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for(T object:objects){
                        getDaoSession().delete(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    /**************************数据库查询操作***********************/

    /**
     * 获得某个表名
     * @return
     */
    public String getTablename(Class object){
        return getDaoSession().getDao(object).getTablename();
    }

    /**
     * 根据主键ID来查询
     * @param id
     * @return
     */
    public <T> T queryById(long id,Class object){
        return (T)getDaoSession().getDao(object).loadByRowId(id);
    }

    /**
     * 查询某条件下的对象
     * @param object
     * @return
     */
    public List queryObject(Class object,String where,String...params){
        Object obj = null;
        List objects = null;
        try {
            obj = getDaoSession().getDao(object);
            if (null == obj){
                return null;
            }
            objects = getDaoSession().getDao(object).queryRaw(where,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    /**
     * 查询所有对象
     * @param object
     * @return
     */
    public List queryAll(Class object){
        List objects = null;
        try {
            objects = (List) getDaoSession().getDao(object).loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    /**
     * 查询某个ID的对象是否存在
     * @param     property  数据属性,例如 UserDao.Properties.Id
     * @return
     */
    public <T extends AbstractDao>boolean isExist(long id, Class object, Property property){
        QueryBuilder qb = (QueryBuilder) getDaoSession().getDao(object).queryBuilder();
        qb.where(property.eq(id));
        long length = qb.buildCount().count();
        return length>0 ? true:false;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase(){
        closeHelper();
        closeDaoSession();
    }

    public void closeDaoSession(){
        if (null != mDaoSession){
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void closeHelper(){
        if (openHelper != null){
            openHelper.close();
            openHelper = null;
        }
    }
}
