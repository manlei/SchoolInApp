package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.Avatar;
import cn.edu.nju.cs.seg.service.AvatarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by fwz on 2017/7/8.
 */
@Controller
@RequestMapping("/api")
public class MediaController {
    @RequestMapping(value = "/avatars/{avatarId}", method = RequestMethod.GET)
    public String getAvatar(
            @PathVariable("avatarId") int avatarId) {
        Avatar avatar = AvatarService.findAvatarById(avatarId);
        if (avatarId == 1 || avatar == null) { //默认头像
            return ServerConfig.AVATAR_DIR + "1.png";
        } else {
            return ServerConfig.AVATAR_DIR + avatar.getMd5() + "." + avatar.getSuffix();
        }
    }
}
