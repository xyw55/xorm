package com.xyw55;

import com.xyw55.po.Notes;
import com.xyw55.vo.NoteVO;
import com.xyw55.xorm.core.MysqlQuery;
import com.xyw55.xorm.core.QueryFactory;

import java.util.List;

/**
 * Created by xiayiwei on 7/27/17.
 */
public class testOrm {

    public static void testDML(String[] args) {
//        Notes notes = new Notes();
//        notes.setId(2);
//        new MysqlQuery().delete(notes);

//        Notes notes = new Notes();
//        notes.setAuthor("55");
//        notes.setContent("lalalala");
//        notes.setTitle("test2");
//        notes.setTag("o");
//        notes.setCreateTime(new Date(System.currentTimeMillis()));
//        new MysqlQuery().insert(notes);

//        notes.setId(4);
//        new MysqlQuery().update(notes, new String[]{"title", "content"});
    }

    private static void testQueryRow() {
//        List<Notes> result = new MysqlQuery().queryRows("select id,content from notes where author=?",
//                Notes.class, new Object[]{"55"});
//        for (Notes note : result) {
//            System.out.println(note.getId() + " " + note.getContent());
//        }

        String sql2 = "select u.username, u.password, n.content, n.title from notes n join users u on n.author =u.username";
        List<NoteVO> noteVOs = new MysqlQuery().queryRows(sql2,
                NoteVO.class, null);
        for (NoteVO note : noteVOs) {
            System.out.println(note.getUsername() + " " + note.getTitle() + " " + note.getContent());
        }
    }

    private static void testQueryFactory() {
        String sql2 = "select u.username, u.password, n.content, n.title from notes n join users u on n.author =u.username";
        List<NoteVO> noteVOs = QueryFactory.getFactory().createQuery().queryRows(sql2,
                NoteVO.class, null);
        for (NoteVO note : noteVOs) {
            System.out.println(note.getUsername() + " " + note.getTitle() + " " + note.getContent());
        }
    }

    private static void testQueryValue() {
        Object obj = new MysqlQuery().queryValue("select count(*) from notes", null);
        System.out.println(obj);

        Number number = new MysqlQuery().queryNumber("select count(*) from notes", null);
        System.out.println(number);

        List<Notes> result = new MysqlQuery().queryRows("select id,content from notes where author=?",
                Notes.class, new Object[]{"55"});
        for (Notes note : result) {
            System.out.println(note.getId() + " " + note.getContent());
        }
    }

    public static void main(String[] args) {
        testQueryRow();
        testQueryFactory();

//        testQueryValue();

    }
}
