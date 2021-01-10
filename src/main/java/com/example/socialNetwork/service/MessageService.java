package com.example.socialNetwork.service;

import com.example.socialNetwork.domain.*;
import com.example.socialNetwork.dto.*;
import com.example.socialNetwork.repo.GeneralMessageRepo;
import com.example.socialNetwork.repo.GroupMessageRepo;
import com.example.socialNetwork.repo.MessageRepo;
import com.example.socialNetwork.repo.PrivateMessageRepo;
import com.example.socialNetwork.util.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageService {
    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepo messageRepo;
    private final GeneralMessageRepo generalMessageRepo;
    private final PrivateMessageRepo privateMessageRepo;
    private final GroupMessageRepo groupMessageRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageService(MessageRepo messageRepo, GeneralMessageRepo generalMessageRepo, PrivateMessageRepo privateMessageRepo, GroupMessageRepo groupMessageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.generalMessageRepo = generalMessageRepo;
        this.privateMessageRepo = privateMessageRepo;
        this.groupMessageRepo = groupMessageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);
    }

    private void fillMeta(Message message) throws IOException {
        String text = message.getText();

        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()){
            String url = text.substring(matcher.start(), matcher.end());
            message.setLink(url);

            matcher = IMG_REGEX.matcher(url);

            if (matcher.find()){
                message.setLinkCover(url);
            } else if (!url.contains("youtu")){
                MetaDto meta = getMeta(url);

                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
                message.setLinkCover(meta.getCover());
            }
        }else {
            message.setLinkDescription("");
            message.setLinkCover("");
            message.setLink("");
        }

    }

    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }

    public void delete(Message message) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    public Message update(Message messageFromDb, Message message) throws IOException {
        messageFromDb.setText(message.getText());
        fillMeta(messageFromDb);
        Message updatedMessage = messageRepo.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }

    public Message create(Message message) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        fillMeta(message);

        if (message.getMessageType().equals(MessageType.GENERAL)){
            GeneralMessage gm = (GeneralMessage) message;
            GeneralMessage updatedMessage = messageRepo.save(gm);
            wsSender.accept(EventType.CREATE, updatedMessage);
            return updatedMessage;
        }

        else if (message.getMessageType().equals(MessageType.GROUP)){
            GroupMessage groupMessage = (GroupMessage) message;
            GroupMessage updatedMessage = messageRepo.save(groupMessage);
            wsSender.accept(EventType.CREATE, updatedMessage);
            return updatedMessage;
        }

        else if (message.getMessageType().equals(MessageType.PRIVATE)){
            PrivateMessage pm = (PrivateMessage) message;
            PrivateMessage updatedMessage = messageRepo.save(pm);
            wsSender.accept(EventType.CREATE, updatedMessage);
            return updatedMessage;
        }

        return null;
    }

    public GeneralMessagePageDto findGeneralMessages(Pageable pageable) {
        Page<GeneralMessage> page = generalMessageRepo.findAll(pageable);
        return new GeneralMessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    public PrivateMessagePageDto findPrivateMessages(User addressee, User author, Pageable pageable) {
        Page<PrivateMessage> page = privateMessageRepo.findAllByAddresseeAndAuthorOrAuthorAndAddressee(addressee, author, author, addressee, pageable);
        return new PrivateMessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    public GroupMessagePageDto findGroupMessages(List<User> addressees, User author, Pageable pageable) {
//        Page<GroupMessage> page = groupMessageRepo.findAllByAddresseesOrAuthor(addressees, author, pageable);
//        return new GroupMessagePageDto(
//                page.getContent(),
//                pageable.getPageNumber(),
//                page.getTotalPages()
//        );

        return null;
    }
}
