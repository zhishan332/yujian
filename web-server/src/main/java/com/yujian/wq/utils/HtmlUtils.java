package com.yujian.wq.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/28
 */
public class HtmlUtils {

    //定义script的正则表达式
    private static String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";

    //定义style的正则表达式
    private static String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";

    //定义HTML标签的正则表达式
    private static String regEx_html = "<[^>]+>";

    //定义空格回车换行符
    private static String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符


    /**
     * 验证名称中是否含有标签，并返回字符串
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 获取标签中的字符串
     *
     * @param htmlStr
     * @return
     */
    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
//htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);
        return htmlStr;
    }

    public static void main(String[] args) {
        String str = "<html>alert(2)</html>";
        System.out.println(getTextFromHtml(str));
    }
}
