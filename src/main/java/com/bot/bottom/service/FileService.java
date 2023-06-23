package com.bot.bottom.service;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.dto.MemDTO;
import com.bot.bottom.model.Mem;
import com.bot.bottom.receiveService.Selector;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {
    private final MemDao memDao;
    private final Selector selector;
    Mem memToDelete;

    public FileService(MemDao memDao, Selector selector) {
        this.memDao = memDao;
        this.selector = selector;
    }


    public Set<String> photoList(String dir){
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

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
    public String askClearFiles(Update update){
        selector.setClearFlag(update.getMessage().getChatId());
        return "Are you sure you want to delete all unmaped files?";
    }

    public String clearFile(Update update, String dir){
        selector.setClearFlag(0);
        long count = 0;
        String text = update.getMessage().getText();
        if(text.equalsIgnoreCase("да") || text.equalsIgnoreCase("yes")){
       count = photoList(dir).stream().map(s -> dir + s).filter(s -> memDao.findDyAddress(s) == null)
                .map(File::new).map(File::delete).count();
                return "Remains only mapper files. \n Deleted " + count + " files";         // maybe add counter
            }
        return "Clearing canceled";
    }


    public boolean removeFile(String address){
        File file = new File(address);
            return file.delete();
    }
}
