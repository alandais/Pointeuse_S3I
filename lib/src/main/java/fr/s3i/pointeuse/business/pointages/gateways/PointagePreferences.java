/*
 * Oburo.O est un programme destinée à saisir son temps de travail sur un support Android.
 *
 *     This file is part of Oburo.O
 *     Oburo.O is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package fr.s3i.pointeuse.business.pointages.gateways;

import fr.s3i.pointeuse.business.communs.gateways.Preferences;

public interface PointagePreferences extends Preferences
{

    public enum Arrondi
    {
        AUCUN, _10_MINUTES, _15_MINUTES, _30_MINUTES, _1HEURE
    }

    public enum DelaiFormat
    {
        JOUR_HEURE_MINUTE, HEURE_MINUTE, DIXIEME_HEURE
    }

    /**
     * Format d'affichage des dates, au format utilisé par la méthode {@link String#format(String, Object...)}.
     *
     * @return Format des dates.
     */
    String getDateFormat();

    /**
     * Format d'affichage des délais.
     *
     * @return Format des délais.
     */
    DelaiFormat getDelaiFormat();

    /**
     * {@code true} pour compter les secondes dans les calculs.
     *
     * @return Mode de précision.
     */
    boolean getPrecision();

    /**
     * Définit comment arrondir les résultats de calculs.
     *
     * @return Type d'arrondi des résultats de calculs.
     */
    Arrondi getArrondi();

    /**
     * Chaîne de caractère à insérer dans le fichier d'export pour séparer les valeurs (ex: ";").
     *
     * @return Séparateur.
     */
    String getExportSeparateur();

    /**
     * Destinataire de l'export de fichier. Suivant l'implémentation choisie, peut être une adresse email, une chaîne de connexion FTP ou autre.
     *
     * @return Le destinataire du fichier export.
     */
    String getExportDestinataire();

    /**
     * Temps minimum à effectuer sur une semaine complète de travail. En dessous, le contrat n'est pas rempli.
     *
     * @return Temps minimum requis par semaine.
     */
    int getTempsTravailMinimumParSemaineEnHeures();

    /**
     * Temps maximum à effectuer sur une semaine complète de travail. Au delà, les heures seront décomptées comme des heures supplémentaires.
     *
     * @return Temps maximum par semaine, hors heures supplémentaires.
     */
    int getTempsTravailMaximumParSemaineEnHeures();

}
