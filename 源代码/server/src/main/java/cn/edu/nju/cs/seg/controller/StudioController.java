package cn.edu.nju.cs.seg.controller;

//import cn.edu.nju.cs.seg.pojo.User;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.exception.BusinessException;
import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.json.JsonMapResponseBuilderFactory;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import cn.edu.nju.cs.seg.util.MD5Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Clypso on 2017/5/2.
 */
@RestController
@RequestMapping("/api")
public class StudioController {
    @RequestMapping(value = "/studios/{studioId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getStudio(@PathVariable("studioId") int studioId) {
        Studio studio = StudioService.findStudioById(studioId);
        Map<String, Object> m;
        if (studio != null) {
            m = JsonMapResponseBuilderFactory
                    .createStudioJsonMapBuilder(studio)
                    .getComplexMap();
            return new ResponseEntity<Map<String, Object>>(m, HttpStatus.OK);
        } else {
            throw new BusinessException("studio not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/studios", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getStudios(HttpServletRequest request) throws ParseException, IOException {
        List<Studio> studios = StudioService.findAllStudios();
        List<Map<String, Object>> l = new ArrayList<>();
        int limit = 50;
        int offset = 0;

        try {
            limit = Integer.parseInt(request.getParameter("limit"));
        } catch (Exception e) {
            limit = 50;
        }

        try {
            offset = Integer.parseInt(request.getParameter("offset"));
        } catch (Exception e) {
            offset = 0;
        }

        String search = request.getParameter("search");
        if (search != null && search.length() > 0) {
            Analyzer analyzer = new StandardAnalyzer();
            Query q = new QueryParser("studioName", analyzer).parse(search);
            int hitsPerPage = 10;
            Directory directory = FSDirectory.open(
                    FileSystems.getDefault().getPath(ServerConfig.LUCENE_INDEX_DIRECTORY));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                org.apache.lucene.document.Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("studioId") + "\t" + d.get("studioName"));
                int studioId = Integer.parseInt(d.get("studioId"));
                Studio studio = StudioService.findStudioById(studioId);
                if (studio != null) {
                    Map<String, Object> m = JsonMapResponseBuilderFactory
                            .createStudioJsonMapBuilder(studio)
                            .getSimpleMap();
                    l.add(m);
                }
            }
            reader.close();
        } else {

            for (int i = offset; i < studios.size() && i < offset + limit; i++) {
                Map<String, Object> map = JsonMapResponseBuilderFactory
                        .createStudioJsonMapBuilder(studios.get(i))
                        .getSimpleMap();
                l.add(map);

            }
        }
        return new ResponseEntity<List<Map<String, Object>>>(l, HttpStatus.OK);


    }


    @RequestMapping(value = "/studios/{studioId}/members", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getStudioMembers(@PathVariable("studioId") int studioId) {
        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            List<User> members = StudioService.findUsersByStudioId(studioId);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (User member : members) {
                Map<String, Object> map = JsonMapResponseBuilderFactory
                        .createUserJsonMapBuilder(member)
                        .getSimpleMap();
                list.add(map);
            }
            return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/studios/{studioId}/questions", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getStudioQuestions(@PathVariable("studioId") int studioId) {
        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            List<Question> questions = QuestionService.findQuestionsByStudioId(studioId);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Question question : questions) {
                Map<String, Object> map = JsonMapResponseBuilderFactory
                        .createQuestionJsonMapBuilder(question)
                        .getSimpleMap();
                list.add(map);
            }

            return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/studios/{studioId}/essays", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getStudioAnswers(@PathVariable("studioId") int studioId) {
        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            List<Essay> essays = EssayService.findEssaysByStudioId(studioId);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Essay essay : essays) {
                Map<String, Object> map = JsonMapResponseBuilderFactory
                        .createEssayJsonMapBuilder(essay)
                        .getSimpleMap();
                list.add(map);
            }
            return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }
    }

    //以上四个没想好

    @RequestMapping(value = "/studios/{studioId}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> putStudio(
            @PathVariable("studioId") int studioId,
            @RequestBody Map<String, Object> bodyMap) throws IOException {
        Map<String, Object> m = new HashMap<>();

        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            String oldManagerEmailOrPhone = (String) bodyMap.get("old_manager_email_or_phone");
            String oldManagerPassword = (String) bodyMap.get("old_manager_password");
            String newManagerEmailOrPhone = (String) bodyMap.get("new_manager_email_or_phone");
            String newManagerPassword = (String) bodyMap.get("new_manager_password");
            User oldManager = UserService.findUserByEmailOrPhone(oldManagerEmailOrPhone);
            User newManager = UserService.findUserByEmailOrPhone(newManagerEmailOrPhone);
            List<User> oldMembers = StudioService.findUsersByStudioId(studioId);
            if (oldManager != null && newManager != null
                    && oldManagerPassword != null
                    && oldManagerPassword.equals(oldManager.getPassword())
                    && oldManager.equals(studio.getManager())
                    && newManagerPassword != null
                    && newManagerPassword.equals(newManager.getPassword())
                    && oldMembers.contains(newManager)) {
                String bio = (String) bodyMap.get("bio");
                String name = (String) bodyMap.get("name");
                if (name != null) {
                    studio.setName(name);
                }
                if (bio != null) {
                    studio.setBio(bio);
                }
                studio.setManager(newManager);
                StudioService.updateStudio(studio);
                if (bodyMap.get("members") != null) {
                    List<String> members = (List<String>) bodyMap.get("members");
                    for (String s : members) {
                        User user = UserService.findUserByEmailOrPhone(s);
                        if (user != null) {
                            StudioService.addMember(studioId, user.getId());
                        }
                    }
                }
                m = new JsonMapBuilder()
                        .append("url", ServerConfig.SERVER_BASE_URL + "/studios/" + studio.getId())
                        .getMap();
                return new ResponseEntity<Map<String, Object>>(m, HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/studios/{studioId}/avatar", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> putUserAvatar(
            HttpServletRequest request,
            @PathVariable("studioId") int studioId,
            @RequestParam("description") String description,
            @RequestParam("avatar") MultipartFile img)
            throws IOException, NoSuchAlgorithmException {
        Studio studio = StudioService.findStudioById(studioId);
        if (studio == null) {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> descriptionMap = new HashMap<String, String>();
        descriptionMap = mapper.readValue(description, descriptionMap.getClass());
        String managerPassword = (String) descriptionMap.get("manager_password");
        String managerEmailOrPhone = (String) descriptionMap.get("manager_email_or_phone");
        User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);
        if (manager == null || managerPassword == null
                || !studio.getManager().equals(manager)
                || !studio.getManager().getPassword().equals(manager.getPassword())) {
            throw new BusinessException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        String md5 = MD5Util.getMultipartFileMD5(img);
        Avatar avatar1 = AvatarService.findAvatarByMd5(md5);
        long avatarId = 1;
        if (avatar1 != null) {
            avatarId = avatar1.getId();
        } else {

            String avatarDir = request.getSession().getServletContext().getRealPath("/")
                    + "avatars/";
            String originalFilename = img.getOriginalFilename();
            String suffix = originalFilename.substring(
                    originalFilename.lastIndexOf(".") + 1);
            Avatar avatar = new Avatar(md5, suffix);
            AvatarService.add(avatar);
            avatarId = avatar.getId();
            img.transferTo(new File(avatarDir + md5 + "." + suffix));

        }
        studio.setAvatar((int) avatarId);
        StudioService.updateStudio(studio);

        Map<String, Object> map = new JsonMapBuilder()
                .append("url", ServerConfig.SERVER_BASE_URL + "/avatars/" + avatarId)
                .getMap();
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/studios/{studioId}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> patchStudio(
            @PathVariable("studioId") int studioId,
            @RequestBody Map<String, Object> requestBody) throws IOException {
        Map<String, Object> m;

        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            String managerEmailOrPhone = (String) requestBody.get("manager_email_or_phone");
            String managerPassword = (String) requestBody.get("manager_password");

            User oldManager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);
            if (oldManager != null && managerPassword != null
                    && managerPassword.equals(oldManager.getPassword())
                    && oldManager.equals(studio.getManager())) {
                String newManagerEmailOrPhone = (String) requestBody.get("new_manager_email_or_phone");
                String newManagerPassword = (String) requestBody.get("new_manager_password");
                String bio = (String) requestBody.get("bio");
                String avatar = (String) requestBody.get("avatar");
                String name = (String) requestBody.get("name");

                if (requestBody.get("top_question") != null) {
                    int questionId = (int) requestBody.get("top_question");
                    Question question = QuestionService.findQuestionById(questionId);
                    List<Question> questions = QuestionService.findQuestionsByStudioId(studioId);
                    if (question != null && questions != null && questions.contains(question)) {
                        studio.setTopQuestionId(question.getId());
                    }
                }
                if (requestBody.get("top_essay") != null) {
                    int essayId = (int) requestBody.get("top_essay");
                    Essay essay = EssayService.findEssayById(essayId);
                    List<Essay> essays = EssayService.findEssaysByStudioId(studioId);
                    if (essay != null && essays != null && essays.contains(essay)) {
                        studio.setTopEssayId(essayId);
                    }
                }
                List<User> oldMembers = StudioService.findUsersByStudioId(studioId);

                User newManager = UserService.findUserByEmailOrPhone(newManagerEmailOrPhone);
                if (newManager != null) {
                    if (newManagerPassword != null
                                    && newManagerPassword.equals(newManagerPassword)
                                    && oldMembers.contains(newManager)) {
                        studio.setManager(newManager);
                    } else {
                        throw new BusinessException("Invalid new manager account or password", HttpStatus.UNAUTHORIZED);
                    }
                }


                if (name != null) {
                    studio.setName(name);
                }
                if (bio != null) {
                    studio.setBio(bio);
                }

                if (avatar != null && avatar.contains("/avatars/")) {
                    int avatarId = Integer.parseInt(avatar
                            .substring(avatar.indexOf("/avatars/") + "/avatars/".length()));
                    studio.setAvatar(avatarId != -1 ? avatarId : 1);
                }

                StudioService.updateStudio(studio);

                if (requestBody.get("members") != null) {
                    List<String> members = (List<String>) requestBody.get("members");
                    for (String account : members) {
                        User user = UserService.findUserByEmailOrPhone(account);
                        if (user != null) {
                            StudioService.addMember(studioId, user.getId());
                        }
                    }
                }
                m = new JsonMapBuilder()
                        .append("url", ServerConfig.SERVER_BASE_URL + "/studios/" + studio.getId())
                        .getMap();
                return new ResponseEntity<Map<String, Object>>(m, HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/studios", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postStudios(
            @RequestBody Map<String, Object> bodyMap) throws IOException {
        Map<String, Object> m;
        String managerEmailOrPhone = (String) bodyMap.get(("manager_email_or_phone"));
        String managerPassword = (String) bodyMap.get("manager_password");
        User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);

        if (manager != null && managerPassword != null
                && managerPassword.equals(manager.getPassword())) {

            if (bodyMap.get("name") != null) {
                Studio studio = new Studio((String) bodyMap.get("name"), manager);
                String bio = (String) bodyMap.get("bio");

                if (bio != null) {
                    studio.setBio(bio);
                }

                int studioId = StudioService.add(studio);
                StudioService.addMember(studioId, manager.getId());
                if (bodyMap.get("members") != null) {
                    List<String> members = (List<String>) bodyMap.get("members");
                    for (String s : members) {
                        User user;
                        if (s.contains("@")) {
                            user = UserService.findUserByEmail(s);
                        } else {
                            user = UserService.findUserByPhone(s);
                        }
                        if (user != null) {
                            StudioService.addMember(studioId, user.getId());
                        }
                    }
                }

                m = new HashMap<String, Object>();
                m.put("url", ServerConfig.SERVER_BASE_URL + "/studios/" + studioId);
                return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
            } else {
                throw new BusinessException("require stuio name", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/studios/application", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postStudioApplication(
            @RequestBody Map<String, Object> requestBody) {
        String managerEmailOrPhone = (String) requestBody.get(("manager_email_or_phone"));
        String managerPassword = (String) requestBody.get("manager_password");
        String name = (String) requestBody.get("name");
        String qualification = (String) requestBody.get("qualification");
        User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);
        if (manager != null
                && managerPassword != null
                && managerPassword.equals(manager.getPassword())) {
            if (name != null && qualification != null) {
                String bio = (String) requestBody.get("bio");
                String avatar = (String) requestBody.get("avatar");
                String info = new JsonMapBuilder()
                        .append("name", name)
                        .append("manager_id", manager.getId())
                        .append("qualification", qualification)
                        .append("bio", bio)
                        .append("avatar_url", avatar)
                        .toString();
                System.out.println(info);
                return new ResponseEntity<Map<String, Object>>(HttpStatus.ACCEPTED);

            } else {
                throw new BusinessException("Bad request", HttpStatus.BAD_REQUEST);
            }

        } else {
            throw new BusinessException("Invalid account or password", HttpStatus.UNAUTHORIZED);
        }


    }

    @RequestMapping(value = "/studios/{studioId}/members", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postStudioMembers(
            @PathVariable int studioId, @RequestBody Map<String, Object> requestBody) {

        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            String managerEmailOrPhone = (String) requestBody.get("manager_email_or_phone");
            String managerPassword = (String) requestBody.get("manager_password");
            String memberEmailOrPhone = (String) requestBody.get("member_email_or_phone");
            User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);
            if (manager != null && manager.equals(studio.getManager())
                    && managerPassword != null
                    && managerPassword.equals(manager.getPassword())) {
                User member = UserService.findUserByEmailOrPhone(memberEmailOrPhone);
                if (member != null) {
                    StudioService.addMember(studioId, member.getId());
                    return new ResponseEntity<Map<String, Object>>(HttpStatus.CREATED);
                } else {
                    throw new BusinessException("Member not found", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/studios/{studioId}/essays", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postStudioEssays(
            @PathVariable int studioId,
            HttpServletRequest request,
            @RequestParam("description") String description,
            @RequestParam("photo") List<MultipartFile> files,
            @RequestParam("audio") List<MultipartFile> audio)
            throws IOException, NoSuchAlgorithmException {
        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> descriptionMap = new HashMap<String, Object>();
            descriptionMap = mapper.readValue(description, descriptionMap.getClass());

            String managerEmailOrPhone = (String) descriptionMap.get("manager_email_or_phone");
            String managerPassword = (String) descriptionMap.get("manager_password");
            String essayTitle = (String) descriptionMap.get("essay_title");
            String essayContent = (String) descriptionMap.get("essay_content");
            String type = (String) descriptionMap.get("type");
            User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);
            if (manager != null && managerPassword != null
                    && studio.getManager().equals(manager)
                    && managerPassword.equals(manager.getPassword())) {

                Essay essay = null;
                if ("text".equals(type)) {

                    String imageDir = request.getSession().getServletContext().getRealPath("/")
                            + "images/";
                    List<String> names = new ArrayList<String>();
                    for (MultipartFile file : files) {
                        String md5 = MD5Util.getMultipartFileMD5(file);
                        String originalFilename = file.getOriginalFilename();
                        String suffix = originalFilename.substring(
                                originalFilename.lastIndexOf("."));
                        names.add(md5 + suffix);
                        file.transferTo(new File(imageDir + md5 + suffix));
                    }

                    Document document = Jsoup.parse(essayContent);
                    int i = 0;
                    Elements images = document.getElementsByTag("img");
                    for (Element image : images) {
                        String oldSrc = image.attr("src");
                        image.attr("src",
                                ServerConfig.IMAGES_BASE_URL + names.get(i++));
                    }

                    essay = new Essay(essayTitle != null ? essayTitle : "",
                            document.toString(), studio, Essay.TYPE_TEXT);
                } else {
                    String imageDir = request.getSession().getServletContext().getRealPath("/")
                            + "audios/";
                    String md5 = MD5Util.getMultipartFileMD5(audio.get(0));
                    String originalFilename = audio.get(0).getOriginalFilename();
                    String suffix = originalFilename.substring(
                            originalFilename.lastIndexOf("."));
                    audio.get(0).transferTo(new File(imageDir + md5 + suffix));
                    essay = new Essay(essayTitle, ServerConfig.AUDIOS_BASE_URL + md5 + suffix,
                            studio, Essay.TYPE_AUDIO);
                }
                int essayId = EssayService.add(essay);
                Map<String, Object> map = new JsonMapBuilder()
                        .append("url", ServerConfig.SERVER_BASE_URL + "/essays/" + essayId)
                        .getMap();
                return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
            } else {
                throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/studios/{studioId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteStudio(
            @PathVariable("studioId") int studioId,
            @RequestBody Map<String, Object> requestBody) throws IOException {
        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            String managerEmailOrPhone = (String) requestBody.get("manager_email_or_phone");
            String managerPassword = (String) requestBody.get("manager_password");
            User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);
            if (manager != null && managerPassword != null
                    && studio.getManager().equals(manager)
                    && managerPassword.equals(manager.getPassword())) {
                List<User> members = StudioService.findUsersByStudioId(studioId);
                for (User member : members) {
                    StudioService.removeMember(studioId, member.getId());
                }
                StudioService.remove(studioId);
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);

            } else {
                throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/studios/{studioId}/members", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteStudioMembers(
            @PathVariable("studioId") int studioId,
            @RequestBody Map<String, Object> requestBody) {
        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            String managerEmailOrPhone = (String) requestBody.get("manager_email_or_phone");
            String managerPassword = (String) requestBody.get("manager_password");
            User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);
            if (manager != null && managerPassword != null
                    && studio.getManager().equals(manager)
                    && managerPassword.equals(manager.getPassword())) {
                if (requestBody.get("members") != null) {
                    List<String> members = (List<String>) requestBody.get("members");
                    for (String memberAccount : members) {
                        User member = UserService.findUserByEmailOrPhone(memberAccount);
                        if (member != null) {
                            StudioService.removeMember(studioId, member.getId());
                        }
                    }
                }
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/studios/{studioId}/members/{memberId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteStudioMembers(
            @PathVariable("studioId") int studioId,
            @PathVariable("memberId") int memberId,
            @RequestBody Map<String, Object> requestBody) {
        Studio studio = StudioService.findStudioById(studioId);
        if (studio != null) {
            String managerEmailOrPhone = (String) requestBody.get("manager_email_or_phone");
            String managerPassword = (String) requestBody.get("manager_password");
            User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);
            if (manager != null && managerPassword != null
                    && studio.getManager().equals(manager)
                    && managerPassword.equals(manager.getPassword())) {
                User member = UserService.findUserById(memberId);
                List<User> members = StudioService.findUsersByStudioId(studioId);
                if (member != null && members.contains(member)) {
                    StudioService.removeMember(studioId, memberId);
                    return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
                } else {
                    throw new BusinessException("Studio member not found", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Studio not found", HttpStatus.NOT_FOUND);
        }
    }

}