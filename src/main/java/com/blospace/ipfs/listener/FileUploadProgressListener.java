package com.blospace.ipfs.listener;

import com.blospace.ipfs.progress.Progress;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * <dl>
 * <dt>ipfs_server</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2018年04月12日</dd>
 * </dl>
 *
 * @author LuoHui
 */
@Component
public class FileUploadProgressListener implements ProgressListener {

    private HttpSession session;

    public void setSession(HttpSession session) {
        this.session = session;
        Progress status = new Progress();//保存上传状态
        session.setAttribute("status", status);
    }

    @Override
    public void update(long bytesRead, long contentLength, int items) {
        Progress status = (Progress) session.getAttribute("status");
        status.setBytesRead(bytesRead);
        status.setContentLength(contentLength);
        status.setItems(items);
    }
}
