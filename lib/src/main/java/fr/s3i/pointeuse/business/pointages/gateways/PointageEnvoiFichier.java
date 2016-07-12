package fr.s3i.pointeuse.business.pointages.gateways;

import fr.s3i.pointeuse.business.communs.gateways.System;

public interface PointageEnvoiFichier extends System
{

    void envoyer(String destinataire, String sujet, String corps, String nomFichier, byte[] pieceJointe);

}
