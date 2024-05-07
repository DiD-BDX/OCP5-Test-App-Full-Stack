// Importation des dépendances nécessaires pour les tests
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';

// Définition du groupe de tests pour le composant LoginComponent
describe('LoginComponent', () => {
  // Déclaration des variables pour le composant et son environnement de test
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  // Configuration initiale avant chaque test
  beforeEach(async () => {
    // Création des mocks pour les services
    const authServiceMock = {
      login: jest.fn().mockReturnValue(of({}))
    };

    const sessionServiceMock = {
      logIn: jest.fn()
    };

    const routerMock = {
      navigate: jest.fn()
    };

    // Configuration du module de test avec le composant à tester et les services mockés
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();

    // Création de l'environnement de test pour le composant
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  // Test pour vérifier que le composant est bien créé
  test('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier que la méthode login() de AuthService est appelée quand submit() est appelée
  test('should call login on the AuthService when submit is called', () => {
    // Configuration du formulaire
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['password'].setValue('password');

    // Appel de la méthode à tester
    component.submit();

    // Vérification que la méthode login() a été appelée
    expect(authService.login).toHaveBeenCalled();
  });

  // Test pour vérifier que la méthode logIn() de SessionService est appelée quand le login est réussi
  test('should call logIn on the SessionService when login is successful', () => {
    // Configuration du formulaire
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['password'].setValue('password');

    // Appel de la méthode à tester
    component.submit();

    // Vérification que la méthode logIn() a été appelée
    expect(sessionService.logIn).toHaveBeenCalled();
  });

  // Test pour vérifier que la méthode navigate() de Router est appelée avec ['/sessions'] quand le login est réussi
  test('should navigate to /sessions when login is successful', () => {
    // Configuration du formulaire
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['password'].setValue('password');

    // Appel de la méthode à tester
    component.submit();

    // Vérification que la méthode navigate() a été appelée avec ['/sessions']
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  // Test pour vérifier que onError est mis à true quand le login échoue
  it('should set onError to true when login fails', () => {
    // Configuration du formulaire et du mock pour simuler une erreur
    authService.login = jest.fn().mockReturnValue(throwError(() => new Error('error')));
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['password'].setValue('password');

    // Appel de la méthode à tester
    component.submit();

    // Vérification que onError est true
    expect(component.onError).toBe(true);
  });

  // Test pour vérifier que le champ email est invalide quand il est vide
  test('should set email field as invalid when it is empty', () => {
    // Configuration du champ email
    component.form.controls['email'].setValue('');

    // Déclenchement de la détection de changements
    fixture.detectChanges();

    // Vérification que le champ email est invalide
    expect(component.form.controls['email'].invalid).toBe(true);
  });

  // Test pour vérifier que le champ password est invalide quand il est vide
  test('should set password field as invalid when it is empty', () => {
    // Configuration du champ password
    component.form.controls['password'].setValue('');

    // Déclenchement de la détection de changements
    fixture.detectChanges();

    // Vérification que le champ password est invalide
    expect(component.form.controls['password'].invalid).toBe(true);
  });
});