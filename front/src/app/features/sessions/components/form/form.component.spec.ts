import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { TeacherService } from '../../../../services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpTestingController: HttpTestingController;
  let formBuilder: FormBuilder;

  const sessionApiServiceMock = {
    detail: jest.fn().mockReturnValue(of({})),
    create: jest.fn().mockReturnValue(of({})),
    update: jest.fn().mockReturnValue(of({}))
  };

  const sessionServiceMock = {
    sessionInformation: { admin: true }
  };

  const routerMock = {
    navigate: jest.fn(),
    url: '/update'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FormComponent],
      providers: [
        FormBuilder,
        MatSnackBar,
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        TeacherService,
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    httpTestingController = TestBed.inject(HttpTestingController);
    formBuilder = new FormBuilder();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpTestingController.verify(); // Vérifiez qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should initialize form for update', () => { // test d'integration
    expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('1');
    expect(component.onUpdate).toBe(true);
  });

  test('should submit form for create', () => { // test d'integration
    component.onUpdate = false;
    component.submit();
    expect(sessionApiServiceMock.create).toHaveBeenCalled();
  });

  test('should submit form for update', () => { // test d'integration
    component.onUpdate = true;
    component.submit();
    expect(sessionApiServiceMock.update).toHaveBeenCalled();
  });

  test('should be invalid when a required field is missing', () => {
    // Rendre le formulaire invalide
    const formBuilder = new FormBuilder();
    component.sessionForm = formBuilder.group({
      name: ['', Validators.required]
    });
    component.submit();
    expect(component.sessionForm?.valid).toBeFalsy();
  });
});