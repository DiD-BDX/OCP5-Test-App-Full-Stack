// Importation des dépendances nécessaires pour les tests
import { ComponentFixture, TestBed } from '@angular/core/testing'; // Outils de test Angular
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest

import { NotFoundComponent } from './not-found.component'; // Le composant à tester

// Définition du groupe de tests pour le composant NotFoundComponent
describe('NotFoundComponent', () => {
  // Déclaration des variables pour le composant et son environnement de test
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;

  // Configuration initiale avant chaque test
  beforeEach(async () => {
    // Configuration du module de test avec le composant à tester
    await TestBed.configureTestingModule({
      declarations: [ NotFoundComponent ]
    })
    .compileComponents(); // Compilation des composants

    // Création de l'environnement de test pour le composant
    fixture = TestBed.createComponent(NotFoundComponent);
    component = fixture.componentInstance; // Instance du composant
    fixture.detectChanges(); // Déclenchement de la détection de changements
  });

  // Test pour vérifier que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});