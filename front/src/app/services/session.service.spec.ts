// Importation des dépendances nécessaires pour les tests
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { SessionService } from './session.service'; // Service pour la gestion des sessions
import { SessionInformation } from '../interfaces/sessionInformation.interface'; // Interface pour les informations de session

// Définition du groupe de tests pour le service SessionService
describe('SessionService', () => {
  // Déclaration de la variable pour le service
  let service: SessionService;

  // Configuration initiale avant chaque test
  beforeEach(() => {
    service = new SessionService(); // Création d'une nouvelle instance du service
  });

  // Test pour vérifier que le service est bien créé
  test('should create', () => {
    expect(service).toBeTruthy();
  });

  // Test pour vérifier que le statut de connexion est retourné correctement
  test('should return logged in status', (done) => {
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false); // On s'attend à ce que l'utilisateur ne soit pas connecté initialement
      done();
    });
  });

  // Test pour vérifier que l'utilisateur est bien connecté
  test('should log in user', (done) => {
    const user: SessionInformation = { id: 1, username: 'Test User', admin: true, token: 'mock', type: 'mock', firstName: 'Test', lastName: 'User' };
    service.logIn(user); // Connexion de l'utilisateur

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true); // On s'attend à ce que l'utilisateur soit connecté
      expect(service.sessionInformation).toEqual(user); // On s'attend à ce que les informations de session correspondent à l'utilisateur connecté
      done();
    });
  });

  // Test pour vérifier que l'utilisateur est bien déconnecté
  test('should log out user', (done) => {
    service.logOut(); // Déconnexion de l'utilisateur

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false); // On s'attend à ce que l'utilisateur ne soit pas connecté
      expect(service.sessionInformation).toBeUndefined(); // On s'attend à ce que les informations de session soient indéfinies
      done();
    });
  });
});