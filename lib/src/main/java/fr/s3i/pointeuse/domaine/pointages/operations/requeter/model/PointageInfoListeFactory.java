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

package fr.s3i.pointeuse.domaine.pointages.operations.requeter.model;

import java.util.ArrayList;
import java.util.List;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.services.Service;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapper;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperListe;

/**
 * Created by Adrien on 30/07/2016.
 */
public class PointageInfoListeFactory implements Service {

    private final PointageInfoFactory pointageInfoFactory;

    public PointageInfoListeFactory(Contexte contexte) {
        this.pointageInfoFactory = contexte.getService(PointageInfoFactory.class);
    }

    public PointageInfoListe getPointageInfoListe(PointageWrapperListe listePointageWrapper) {
        String dureeTotale = listePointageWrapper.getDureeTotale();
        List<PointageWrapper> liste = listePointageWrapper.getPointages();
        List<PointageInfo> listePointageInfo = new ArrayList<>();
        for (PointageWrapper pointageWrapper : liste) {
            listePointageInfo.add(pointageInfoFactory.getPointageInfo(pointageWrapper));
        }
        return new PointageInfoListe(listePointageInfo, dureeTotale);
    }

}