package xyz.brassgoggledcoders.podium.api.event;

import net.minecraftforge.eventbus.api.Event;
import xyz.brassgoggledcoders.podium.api.bookholder.IBookHolder;

public class GetPageContentsEvent extends Event {
    private final IBookHolder bookHolder;
    private String pageContent;

    public GetPageContentsEvent(IBookHolder bookHolder) {
        this.bookHolder = bookHolder;
        this.pageContent = null;
    }

    public IBookHolder getBookHolder() {
        return bookHolder;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }
}
