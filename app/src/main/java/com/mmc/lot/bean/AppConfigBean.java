package com.mmc.lot.bean;

import java.io.Serializable;

/**
 * Created by ll on 2017/11/26.
 * 配置常量
 */

public class AppConfigBean implements Serializable {
    private static final long serialVersionUID = -29049305364222666L;

    /**
     * leftHeadIconUrl : http://p3.ifengimg.com/a/2017_47/197183ef8669b74.jpg
     * push : {"winButtonText":"弹窗","remindNun":5,"playVideoNum":5,"winStatus":1,"winContent":"弹窗弹窗弹窗弹窗弹窗弹窗弹窗弹窗弹窗弹窗","winTitle":"这是一个弹窗","winImg":"http://p0.ifengimg.com/a/2017_47/6b48d78b7f2d227.jpg","likeNum":5}
     * leftHeadShow : 1
     * showReprint : 1
     */
    public static String LEFTHEADICONURL = "leftHeadIconUrl";
    public static String WINBUTTONTEXT = "winButtonText";
    public static String REMINDNUN = "remindNun";
    public static String PLAYVIDEONUM = "playVideoNum";
    public static String WINSTATUS = "winStatus";
    public static String WINCONTENT = "winContent";
    public static String WINTITLE = "winTitle";
    public static String WINIMG = "winImg";
    public static String LIKENUM = "likeNum";
    public static String LEFTHEADSHOW = "leftHeadShow";
    public static String SHOWREPRINT = "showReprint";
    public static String DOUBLELIKENUM = "doubleLikeNum";
    public static String ICON_NEED_LOGIN_SHARE = "ICON_NEED_LOGIN_SHARE";
	public static String ICON_NEED_LOGIN_REPORT = "ICON_NEED_LOGIN_REPORT";
	public static String ICON_NEED_LOGIN_BACK = "ICON_NEED_LOGIN_BACK";
	public static String ICON_NEED_LOGIN_COPYURL = "ICON_NEED_LOGIN_COPYURL";

    private String leftHeadIconUrl;//左上角ICON url
    private PushBean push;
    private int leftHeadShow;//左上角是否显示icon "1"显示，"0"不显示
    private int showReprint;//是否显示转载标签
    private int doubleLikeNum;////双击点赞次数
    private IconNeedLoginBean iconNeedLogin; //标签功能是否需要登陆 “1”需要 “0”不需要

    public int getDoubleLikeNum() {
        return doubleLikeNum;
    }

    public void setDoubleLikeNum(int doubleLikeNum) {
        this.doubleLikeNum = doubleLikeNum;
    }

    public String getLeftHeadIconUrl() {
        return leftHeadIconUrl;
    }

    public void setLeftHeadIconUrl(String leftHeadIconUrl) {
        this.leftHeadIconUrl = leftHeadIconUrl;
    }

    public PushBean getPush() {
        return push;
    }

    public void setPush(PushBean push) {
        this.push = push;
    }

    public int getLeftHeadShow() {
        return leftHeadShow;
    }

    public void setLeftHeadShow(int leftHeadShow) {
        this.leftHeadShow = leftHeadShow;
    }

    public int getShowReprint() {
        return showReprint;
    }

    public void setShowReprint(int showReprint) {
        this.showReprint = showReprint;
    }

	public IconNeedLoginBean getIconNeedLogin() {
		return iconNeedLogin;
	}

	public void setIconNeedLogin(IconNeedLoginBean iconNeedLogin) {
		this.iconNeedLogin = iconNeedLogin;
	}

	public static class PushBean implements Serializable{
        private static final long serialVersionUID = 7202035760912599867L;
        /**
         * winButtonText : 弹窗
         * remindNun : 5
         * playVideoNum : 5
         * winStatus : 1
         * winContent : 弹窗弹窗弹窗弹窗弹窗弹窗弹窗弹窗弹窗弹窗
         * winTitle : 这是一个弹窗
         * winImg : http://p0.ifengimg.com/a/2017_47/6b48d78b7f2d227.jpg
         * likeNum : 5
         */

        private String winButtonText;//弹窗按钮文字
        private int remindNun;//提示次数
        private int playVideoNum;//播放视频数
        private int winStatus;//弹窗开关 "1"开，"0"关
        private String winContent;//弹窗内容
        private String winTitle;//弹窗标题
        private String winImg;//弹窗图标
        private int likeNum;//未登录时点击喜欢个数

        public String getWinButtonText() {
            return winButtonText;
        }

        public void setWinButtonText(String winButtonText) {
            this.winButtonText = winButtonText;
        }

        public int getRemindNun() {
            return remindNun;
        }

        public void setRemindNun(int remindNun) {
            this.remindNun = remindNun;
        }

        public int getPlayVideoNum() {
            return playVideoNum;
        }

        public void setPlayVideoNum(int playVideoNum) {
            this.playVideoNum = playVideoNum;
        }

        public int getWinStatus() {
            return winStatus;
        }

        public void setWinStatus(int winStatus) {
            this.winStatus = winStatus;
        }

        public String getWinContent() {
            return winContent;
        }

        public void setWinContent(String winContent) {
            this.winContent = winContent;
        }

        public String getWinTitle() {
            return winTitle;
        }

        public void setWinTitle(String winTitle) {
            this.winTitle = winTitle;
        }

        public String getWinImg() {
            return winImg;
        }

        public void setWinImg(String winImg) {
            this.winImg = winImg;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }
    }

    // 标签功能是否需要登陆 “1”需要 “0”不需要
	// "iconNeedLogin" : {
	// 	    "share" : 0,
	// 		"report" : 0,
	// 		"back" : 0,
	// 		"copyUrl" : 0,
	// }
	public static class IconNeedLoginBean implements Serializable {
		private static final long serialVersionUID = -7202035760912599867L;

	    private int share;
	    private int report;
	    private int back;
	    private int copyUrl;

	    public int getShare() {
		    return share;
	    }

	    public void setShare(int share) {
		    this.share = share;
	    }

	    public int getReport() {
		    return report;
	    }

	    public void setReport(int report) {
		    this.report = report;
	    }

	    public int getBack() {
		    return back;
	    }

	    public void setBack(int back) {
		    this.back = back;
	    }

	    public int getCopyUrl() {
		    return copyUrl;
	    }

	    public void setCopyUrl(int copyUrl) {
		    this.copyUrl = copyUrl;
	    }
    }
}
