package fr.utc.nf28.moka;

import android.app.Application;
import fr.utc.nf28.moka.data.ComputerType;
import fr.utc.nf28.moka.data.MediaType;
import fr.utc.nf28.moka.data.MokaType;
import fr.utc.nf28.moka.data.TextType;

import java.util.HashMap;

public class MokaApplication extends Application {
	public static HashMap<String, MokaType> MOKA_TYPES;

	@Override
	public void onCreate() {
		super.onCreate();

		MOKA_TYPES = new HashMap<String, MokaType>() {
			{
				put(MediaType.ImageType.KEY_TYPE, new MediaType.ImageType("Image", "Description d'une image"));
				put(MediaType.VideoType.KEY_TYPE, new MediaType.VideoType("Vidéo", "Description d'une vidéo"));
				put(MediaType.WebType.KEY_TYPE, new MediaType.WebType("Page web", "Description d'une page web"));
				put(TextType.PlainTextType.KEY_TYPE, new TextType.PlainTextType("Texte", "Description d'un texte"));
				put(TextType.ListType.KEY_TYPE, new TextType.ListType("Liste", "Description d'une liste"));
				put(TextType.PostItType.KEY_TYPE, new TextType.PostItType("Post-it", "Description d'un post-it"));
				put(ComputerType.UmlType.KEY_TYPE, new ComputerType.UmlType("Diagramme UML", "Description d'un diagramme UML"));
			}
		};
	}
}
