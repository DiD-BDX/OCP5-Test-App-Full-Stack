// Importation des dépendances nécessaires pour les tests
import { ComponentFixture, TestBed } from '@angular/core/testing'; // Outils de test Angular
import { ReactiveFormsModule } from '@angular/forms'; // Module pour les formulaires réactifs
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { Router } from '@angular/router'; // Service de routage Angular
import { of, throwError } from 'rxjs'; // Utilitaires RxJS pour créer des Observables
import { RegisterComponent } from './register.component'; // Le composant à tester
import { AuthService } from '../../services/auth.service'; // Le service d'authentification

// Définition du groupe de tests pour le composant RegisterComponent
describe('RegisterComponent', () => {
  // Déclaration des variables pour le composant et son environnement de test
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  // Configuration initiale avant chaque test
  beforeEach(async () => {
    // Création des mocks pour les services
    const authServiceMock = {
      register: jest.fn().mockReturnValue(of({})) // Simule la méthode register
    };

    const routerMock = {
      navigate: jest.fn() // Simule la méthode navigate
    };

    // Configuration du module de test avec le composant à tester et les services mockés
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [ReactiveFormsModule]
    })
      .compileComponents(); // Compilation des composants

    // Création de l'environnement de test pour le composant
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance; // Instance du composant
    authService = TestBed.inject(AuthService); // Injection du service AuthService
    router = TestBed.inject(Router); // Injection du service Router
    fixture.detectChanges(); // Déclenchement de la détection de changements
  });

  // Test pour vérifier que le composant est bien créé
  test('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier que la méthode register() de AuthService est appelée quand submit() est appelée
  test('should call register on the AuthService when submit is called', () => {
    // Configuration du formulaire
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');
    component.form.controls['password'].setValue('password');

    // Appel de la méthode à tester
    component.submit();

    // Vérification que la méthode register() a été appelée
    expect(authService.register).toHaveBeenCalled();
  });

  // Test pour vérifier que la méthode navigate() de Router est appelée avec ['/login'] quand l'enregistrement est réussi
  test('should navigate to /login when register is successful', () => {
    // Configuration du formulaire
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');
    component.form.controls['password'].setValue('password');

    // Appel de la méthode à tester
    component.submit();

    // Vérification que la méthode navigate() a été appelée avec ['/login']
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  // Test pour vérifier que onError est mis à true quand l'enregistrement échoue
  test('should set onError to true when register fails', () => {
    // Configuration du formulaire et du mock pour simuler une erreur
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('error')));
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');
    component.form.controls['password'].setValue('password');

    // Appel de la méthode à tester
    component.submit();

    // Vérification que onError est true
    expect(component.onError).toBe(true);
  });
});