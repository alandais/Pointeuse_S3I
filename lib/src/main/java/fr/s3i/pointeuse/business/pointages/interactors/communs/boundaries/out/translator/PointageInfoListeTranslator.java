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

package fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.translator;

import java.util.Collection;
import java.util.List;

import fr.s3i.pointeuse.business.communs.Contexte;
import fr.s3i.pointeuse.business.communs.interactors.boundaries.out.model.OutTranslator;
import fr.s3i.pointeuse.business.pointages.entities.Pointage;
import fr.s3i.pointeuse.business.pointages.gateways.PointagePreferences;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.model.PointageInfoListe;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.utils.Calculs;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointageInfoListeTranslator implements OutTranslator<List<Pointage>, PointageInfoListe> {

    private final PointageInfoTranslator pointageInfoTranslator;

    private final PointagePreferences preferences;

    private final Calculs calculs;

    public PointageInfoListeTranslator(Contexte contexte) {
        this.pointageInfoTranslator = contexte.getService(PointageInfoTranslator.class);
        this.preferences = contexte.getService(PointagePreferences.class);
        this.calculs = contexte.getService(Calculs.class);
    }

    @Override
    public PointageInfoListe translate(List<Pointage> pointages) {
        List<PointageInfo> pointageInfos = pointageInfoTranslator.translate(pointages);
        String formatDuree = calculs.formatDuree(calculs.calculDureeTotale(pointages));
        return new PointageInfoListe(pointageInfos, formatDuree);
    }

    @Override
    public List<PointageInfoListe> translate(Collection<List<Pointage>> entities) {
        return null;
    }
}
