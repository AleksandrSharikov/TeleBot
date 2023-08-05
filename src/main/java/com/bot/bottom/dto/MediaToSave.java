package com.bot.bottom.dto;

import org.telegram.telegrambots.meta.api.methods.GetFile;

public record MediaToSave(GetFile getFile, String address, int type) {
}

