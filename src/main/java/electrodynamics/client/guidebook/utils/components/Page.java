package electrodynamics.client.guidebook.utils.components;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.client.guidebook.utils.pagedata.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.ItemWrapperObject;
import net.minecraft.util.FormattedCharSequence;

public class Page {

	private int pageNumber = 0;
	public List<TextWrapper> text = new ArrayList<>();
	public List<ImageWrapper> images = new ArrayList<>();
	public List<ItemWrapper> items = new ArrayList<>();
	public Chapter associatedChapter;

	public Page(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPage() {
		return pageNumber;
	}

	public static record TextWrapper(int x, int y, FormattedCharSequence characters, int color, boolean centered) {

	}

	public static record ImageWrapper(int x, int y, ImageWrapperObject image) {

	}

	public static record ItemWrapper(int x, int y, ItemWrapperObject item) {

	}

	public static class ChapterPage extends Page {

		public Module associatedModule;

		public ChapterPage(int pageNumber, Module module) {
			super(pageNumber);
			associatedModule = module;
		}

	}

	public static class ModulePage extends Page {

		public ModulePage(int pageNumber) {
			super(pageNumber);
		}

	}

}
