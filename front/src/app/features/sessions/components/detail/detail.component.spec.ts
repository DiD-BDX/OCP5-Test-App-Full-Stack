
import { ComponentFixture, TestBed } from '@angular/core/testing';
//import { MatLegacySnackBarModule as MatSnackBarModule } from '@angular/material/legacy-snack-bar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

import { DetailComponent } from './detail.component';
import { Teacher } from 'src/app/interfaces/teacher.interface';



describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let teacherId: number;
  let teacher: Teacher;

  beforeEach(async () => {
    const sessionInformation = {
      id: 1,
      token: 'mock-token',
      type: 'mock-type',
      username: 'mock-username',
      firstName: 'mock-firstName',
      lastName: 'mock-lastName',
      admin: true,
    };

    const sessionServiceMock = {
      sessionInformation: sessionInformation
    };

    const mockSession = { 
      id: 1,
        name: 'mock-name',
        description: 'mock-description',
        date: new Date(),
        teacher_id: 1,
        users: [1],
        createdAt: new Date(),
        updatedAt: new Date()
    };
    teacher = { id: 1, firstName: 'John', lastName: 'Doe', createdAt: new Date(), updatedAt: new Date()};
    teacherId = 1;
    mockSession.teacher_id = teacherId;

    const sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    };

    const teacherServiceMock = {
      detail: jest.fn().mockReturnValue(of(teacher))
    };

    const matSnackBarMock = {
      open: jest.fn()
    };

    const routerMock = {
      navigate: jest.fn()
    };

    const activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1')
        }
      }
    };

    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
        ]
      })
        .compileComponents();
        fixture = TestBed.createComponent(DetailComponent);
        component = fixture.componentInstance;
        sessionService = TestBed.inject(SessionService);
        sessionApiService = TestBed.inject(SessionApiService);
        teacherService = TestBed.inject(TeacherService);
        matSnackBar = TestBed.inject(MatSnackBar);
        router = TestBed.inject(Router);
        fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should fetch session on init', () => { // test d'integration
    expect(sessionApiService.detail).toHaveBeenCalled();
  });

  test('should fetch teacher when session is fetched', () => { // test d'integration
    expect(teacherService.detail).toHaveBeenCalledWith(teacherId.toString());
    expect(component.teacher).toEqual(teacher);
  });

  test('should delete session and navigate to sessions', () => { // test d'integration
    // Act
    component.delete();
    // Assert
    expect(sessionApiService.delete).toHaveBeenCalled();
    expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  test('should participate in session', () => { // test d'integration
    // Act
    component.participate();

    // Assert
    expect(sessionApiService.participate).toHaveBeenCalled();
  });

  test('should unparticipate in session', () => { // test d'integration
    // Act
    component.unParticipate();

    // Assert
    expect(sessionApiService.unParticipate).toHaveBeenCalled();
  });

  it('should display delete button when user is admin', () => {
    component.isAdmin = true;
    fixture.detectChanges();
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="delete-button"]'));
    expect(deleteButton).toBeTruthy();
  });

  it('should not display delete button when user is not admin', () => {
    component.isAdmin = false;
    fixture.detectChanges();
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="delete-button"]'));
    expect(deleteButton).toBeFalsy();
  });

  it('should call delete method when delete button is clicked', () => {
    component.isAdmin = true;
    fixture.detectChanges();
    jest.spyOn(component, 'delete');
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="delete-button"]')).nativeElement;
    deleteButton.click();
    expect(component.delete).toHaveBeenCalled();
  });

  
});

