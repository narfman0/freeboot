package com.blastedstudios.freeboot.plugin.quest.handler.manifestation;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.plugin.quest.manifestation.dialog.IDialogHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.freeboot.world.DialogManager;

@PluginImplementation
public class DialogHandlerPlugin implements IDialogHandler{
	private DialogManager dialogManager;
	
	public void setDialogManager(DialogManager dialogManager){
		this.dialogManager = dialogManager;
	}

	@Override public CompletionEnum addDialog(String dialog, String origin, String type) {
		dialogManager.add(dialog, origin);
		return CompletionEnum.EXECUTING;
	}

}
