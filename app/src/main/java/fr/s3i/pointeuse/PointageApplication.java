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

package fr.s3i.pointeuse;

import android.app.Application;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.communs.gateways.NotificationSystem;
import fr.s3i.pointeuse.domaine.communs.gateways.ToastSystem;
import fr.s3i.pointeuse.domaine.pointages.ModulePointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointagePreferences;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.persistance.dao.PointageDao;
import fr.s3i.pointeuse.presentation.commun.Vue;
import fr.s3i.pointeuse.presentation.pointer.PointerControleur;
import fr.s3i.pointeuse.presentation.pointer.PointerVue;
import fr.s3i.pointeuse.service.notification.NotificationService;
import fr.s3i.pointeuse.service.notification.ToastService;
import fr.s3i.pointeuse.service.preferences.Preferences;

/**
 * Created by Adrien on 21/07/2016.
 */
public class PointageApplication extends Application {

    private final Contexte contexte;

    private ModulePointage modulePointage;

    public PointageApplication() {
        contexte = new Contexte();
    }

    public Contexte getContexte() {
        return contexte;
    }

    public Vue creerVuePointer() {
        CasUtilisationInfo info =  modulePointage.getInteracteurInfo(PointerControleur.getCasUtilisationClass());
        return PointerVue.getInstance(info.getNom());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contexte.enregistrerService(ToastSystem.class, new ToastService(this.getApplicationContext()));
        contexte.enregistrerService(NotificationSystem.class, new NotificationService(this.getApplicationContext()));
        chargerModulePointage();
    }

    private void chargerModulePointage() {
        contexte.enregistrerService(PointagePreferences.class, new Preferences(this.getApplicationContext()));
        contexte.enregistrerService(PointageRepository.class, new PointageDao(this.getApplicationContext()));
        modulePointage = new ModulePointage(contexte);
    }

}
