module solitaire {
	requires javafx.controls;
	requires transitive javafx.graphics;
	requires static org.junit.jupiter.api;
	exports ca.mcgill.cs.stg.solitaire.gui;
	exports ca.mcgill.cs.stg.solitaire.cards;
}