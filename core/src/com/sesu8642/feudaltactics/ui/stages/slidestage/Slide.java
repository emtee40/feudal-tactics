package com.sesu8642.feudaltactics.ui.stages.slidestage;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

public class Slide {

	private static final float MAX_RESPONSIVE_IMAGE_WIDTH = Gdx.graphics.getDensity() * 1500F;

	private Skin skin;
	private Table table = new Table();

	public Slide(Skin skin, String headline) {
		this.skin = skin;
		// cannot use fillParent because it then the content will be placed a little too high
		table.defaults().pad(10);
		// adding the headline is a hack needed because the slide would get a width of 0 if the the label does not need to wrap (bug?)
		Table hackTable = new Table();
		Label headlineLabel = newNiceLabel(headline);
		headlineLabel.setFontScale(2F);
		hackTable.add(headlineLabel);
		table.add(hackTable);
		table.row();
	}

	public Slide addLabel(String text) {
		Label label = newNiceLabel(text);
		label.setWrap(true);
		table.add(label).fill().expand();
		table.row();
		return this;
	}

	public Slide addImage(String imagePath) {
		Texture imageTexture = new Texture(Gdx.files.internal(imagePath));
		Image image = new Image(imageTexture);
		float aspectRatio = ((float) imageTexture.getHeight()) / ((float) imageTexture.getWidth());
		table.add(image).prefWidth(0).maxWidth(MAX_RESPONSIVE_IMAGE_WIDTH).height(Value.percentWidth(aspectRatio)).expand().fill();
		table.row();
		return this;
	}

	public Slide addTable(List<List<String>> data) {
		Table dataTable = new Table();
		dataTable.defaults().pad(5);
		for (List<String> rowContent : data) {
			for (String cellContent : rowContent) {
				dataTable.add(newNiceLabel(cellContent));
			}
			dataTable.row();
		}
		table.add(dataTable);
		table.row();
		return this;
	}

	public Label newNiceLabel(String content) {
		Label result = new Label(content, skin);
		result.setColor(skin.getColor("black"));
		result.setFontScale(1.5F);
		return result;
	}

	public Table getTable() {
		return table;
	}

}
