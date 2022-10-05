// SPDX-License-Identifier: GPL-3.0-or-later

package de.sesu8642.feudaltactics.frontend;

import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import de.sesu8642.feudaltactics.FeudalTactics;
import de.sesu8642.feudaltactics.backend.editor.EditorInputHandler;
import de.sesu8642.feudaltactics.backend.ingame.LocalIngameInputHandler;
import de.sesu8642.feudaltactics.frontend.dagger.qualifierannotations.AboutScreen;
import de.sesu8642.feudaltactics.frontend.dagger.qualifierannotations.MainMenuScreen;
import de.sesu8642.feudaltactics.frontend.dagger.qualifierannotations.TutorialScreen;
import de.sesu8642.feudaltactics.frontend.events.ScreenTransitionTriggerEvent;
import de.sesu8642.feudaltactics.frontend.ui.screens.GameScreen;
import de.sesu8642.feudaltactics.frontend.ui.screens.IngameScreen;
import de.sesu8642.feudaltactics.frontend.ui.screens.IngameScreenEventHandler;
import de.sesu8642.feudaltactics.frontend.ui.screens.SplashScreen;

/**
 * Controller for navigating between screens.
 */
@Singleton
public class ScreenNavigationController {

	private EventBus eventBus;
	private LocalIngameInputHandler localIngameInputHandler;
	private EditorInputHandler editorInputHandler;
	private SplashScreen splashScreen;
	private IngameScreen ingameScreen;
	private GameScreen mainMenuScreen;
	private GameScreen tutorialScreen;
	private GameScreen aboutScreen;
	private GameScreen preferencesScreen;
	private de.sesu8642.feudaltactics.backend.ingame.EventHandler gameLogicEventHandler;
	private de.sesu8642.feudaltactics.backend.editor.EventHandler editorEventHandler;
	private de.sesu8642.feudaltactics.frontend.renderer.EventHandler rendererEventHandler;
	private IngameScreenEventHandler ingameScreenEventHandler;

	/**
	 * Constructor.
	 * 
	 * @param eventBus                 event bus to register and unregister to/with
	 * @param localIngameInputHandler  local ingame input handler
	 * @param gameLogicEventHandler    game logic event handler
	 * @param splashScreen             splash screen
	 * @param ingameScreen             ingame screen
	 * @param mainMenuScreen           main menu screen
	 * @param rendererEventHandler     renderer event handler
	 * @param preferencesEventHandler  preferences event handler
	 * @param ingameScreenEventHandler ingame screen event handler
	 */
	@Inject
	public ScreenNavigationController(EventBus eventBus, LocalIngameInputHandler localIngameInputHandler,
			EditorInputHandler editorInputHandler, SplashScreen splashScreen, IngameScreen ingameScreen,
			@MainMenuScreen GameScreen mainMenuScreen, @TutorialScreen GameScreen tutorialScreen,
			@AboutScreen GameScreen aboutScreen,
			@de.sesu8642.feudaltactics.frontend.dagger.qualifierannotations.PreferencesScreen GameScreen preferencesScreen,
			de.sesu8642.feudaltactics.backend.ingame.EventHandler gameLogicEventHandler,
			de.sesu8642.feudaltactics.backend.editor.EventHandler editorEventHandler,
			de.sesu8642.feudaltactics.frontend.renderer.EventHandler rendererEventHandler,
			IngameScreenEventHandler ingameScreenEventHandler) {
		this.eventBus = eventBus;
		this.localIngameInputHandler = localIngameInputHandler;
		this.editorInputHandler = editorInputHandler;
		this.splashScreen = splashScreen;
		this.ingameScreen = ingameScreen;
		this.mainMenuScreen = mainMenuScreen;
		this.tutorialScreen = tutorialScreen;
		this.aboutScreen = aboutScreen;
		this.preferencesScreen = preferencesScreen;
		this.gameLogicEventHandler = gameLogicEventHandler;
		this.editorEventHandler = editorEventHandler;
		this.rendererEventHandler = rendererEventHandler;
		this.ingameScreenEventHandler = ingameScreenEventHandler;
	}

	private void unregisterAllEventHandlers() {
		Stream.of(localIngameInputHandler, gameLogicEventHandler, ingameScreenEventHandler, rendererEventHandler)
				.forEach(object -> {
					try {
						eventBus.unregister(object);
					} catch (IllegalArgumentException e) {
						// noop: expected; not all of the objects were registered in the first place
					}
				});
	}

	/**
	 * Event handler for Screen transition events.
	 * 
	 * @param event event to handle
	 */
	@Subscribe
	public void handleScreenTransitionTrigger(ScreenTransitionTriggerEvent event) {
		switch (event.getTransitionTarget()) {
		case SPLASH_SCREEN:
			transitionToSplashScreen();
			break;
		case MAIN_MENU_SCREEN:
			transitionToMainMenuScreen();
			break;
		case INGAME_SCREEN:
			transitionToIngameScreen();
			break;
		case EDITOR_SCREEN:
			transitionToEditorScreen();
			break;
		case TUTORIAL_SCREEN:
			transitionToTutorialScreen();
			break;
		case ABOUT_SCREEN:
			transitionToAboutScreen();
			break;
		case PREFERENCES_SCREEN:
			transitionToPreferencesScreen();
			break;
		default:
			throw new AssertionError("Unimplemented transition target: " + event.getTransitionTarget());
		}
	}

	/** Transitions to the splash screen. */
	public void transitionToSplashScreen() {
		unregisterAllEventHandlers();
		// nothing to register currently
		FeudalTactics.game.setScreen(splashScreen);
	}

	/** Transitions to the main menu screen. */
	public void transitionToMainMenuScreen() {
		unregisterAllEventHandlers();
		// nothing to register currently
		FeudalTactics.game.setScreen(mainMenuScreen);
	}

	/** Transitions to the ingame screen. */
	public void transitionToIngameScreen() {
		unregisterAllEventHandlers();
		Stream.of(localIngameInputHandler, gameLogicEventHandler, ingameScreenEventHandler, rendererEventHandler)
				.forEach(object -> eventBus.register(object));
		FeudalTactics.game.setScreen(ingameScreen);
	}

	/** Transitions to the editor screen. */
	public void transitionToEditorScreen() {
		unregisterAllEventHandlers();
		Stream.of(editorInputHandler, editorEventHandler, ingameScreen, rendererEventHandler)
				.forEach(object -> eventBus.register(object));
		FeudalTactics.game.setScreen(ingameScreen);
	}

	public void transitionToTutorialScreen() {
		unregisterAllEventHandlers();
		FeudalTactics.game.setScreen(tutorialScreen);
	}

	public void transitionToAboutScreen() {
		unregisterAllEventHandlers();
		FeudalTactics.game.setScreen(aboutScreen);
	}

	public void transitionToPreferencesScreen() {
		unregisterAllEventHandlers();
		FeudalTactics.game.setScreen(preferencesScreen);
	}

}