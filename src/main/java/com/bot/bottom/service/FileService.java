package com.bot.bottom.service;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.dto.MemDTO;
import com.bot.bottom.model.Mem;
import com.bot.bottom.receiveService.Selector;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

@Service
public class FileService {
    private final MemDao memDao;
    private final Selector selector;

    public FileService(MemDao memDao, Selector selector) {
        this.memDao = memDao;
        this.selector = selector;
    }
    Mem memToDelete;

    public MemDTO askDeleteFile(Update update){
        String[] toDel = update.getMessage().getText().split("/");
        if(!toDel[0].equals("Delete") || toDel.length != 2){
            return new MemDTO(null, "Request format error");
        }

        if(memDao.findByName(toDel[1].toLowerCase().trim()).isPresent()){
        memToDelete = memDao.findByName(toDel[1].toLowerCase().trim()).get();
            selector.setDeleteFlag(update.getMessage().getChatId());
            return new MemDTO(memToDelete, "Are you sure you want to delete " + memToDelete.getName() + "?") ;
        }
        return new MemDTO(null, "Not found " + toDel[1].toLowerCase().trim() + " to delete") ;
    }

    public String deleteFile(Update update){
        selector.setDeleteFlag(0);
        String text = update.getMessage().getText();
        if(text.equalsIgnoreCase("да") || text.equalsIgnoreCase("yes")){
            if(removeFile(memToDelete.getAddress())){
                memDao.delete(memToDelete);
                memToDelete = null;
                return "deleted";
            }

        }
        return "Deleting canceled";
    }

    private boolean removeFile(String address){
        File file = new File(address);
            return file.delete();
    }

    public Mem getMemToDelete() {
        return memToDelete;
    }
}
