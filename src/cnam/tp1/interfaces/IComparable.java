package cnam.tp1.interfaces;

import cnam.tp1.exception.NotTheSameTypeException;

public interface IComparable<T>{
	/**
	 * Détermine si l'objet comparé est plus grand 
	 * que celui passé en paramètre
	 * @param a
	 * @return vrai si l'appelant est plus grand
	 */
	boolean estPlusGrandQue(T a) throws NotTheSameTypeException;
	/**
	 * Détermine si l'objet comparé est plus petit 
	 * que celui passé en paramètre
	 * @param a
	 * @return vrai si l'appelant est plus petit
	 */
	boolean estPlusPetitQue(T b) throws NotTheSameTypeException;
	/**
	 * Métrique utilisée pour la comparaison (scalaire en précision max). 
	 * @return la valeur sur laquelle s'exerce la comparaison.
	 */
	 double mesure();
}
