
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { ListComponent } from './list.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;

  beforeEach(async () => {
    const sessionInformation: SessionInformation = {
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

    const mockSessions = [
      {
        id: 1,
        name: 'mock-name',
        description: 'mock-description',
        date: new Date(),
        teacher_id: 1,
        users: [1],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];

    const sessionApiServiceMock = {
      all: jest.fn().mockReturnValue(of(mockSessions))
    };

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock }
      ]
    })
      .compileComponents();
      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      sessionService = TestBed.inject(SessionService);
      sessionApiService = TestBed.inject(SessionApiService);
      fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should call all on the SessionApiService when component is created', () => {
    expect(sessionApiService.all).toHaveBeenCalled();
  });

  test('should get sessionInformation from SessionService', () => {
    expect(component.user).toBe(sessionService.sessionInformation);
  });

  test('should show "create" and "update" buttons for admin user', () => {
    // Arrange
    if (sessionService.sessionInformation) {
      sessionService.sessionInformation.admin = true;
    }

    // Act
    fixture.detectChanges();

    // Assert
    const createButton = fixture.debugElement.nativeElement.querySelector('[data-testid="create-button"]');
    const updateButton = fixture.debugElement.nativeElement.querySelector('[data-testid="update-button"]');
    expect(createButton).not.toBeNull();
    expect(updateButton).not.toBeNull();
  });

  test('should not show "create" and "update" buttons for non-admin user', () => {
    // Arrange
    if (sessionService.sessionInformation) {
      sessionService.sessionInformation.admin = false;
    }

    // Act
    fixture.detectChanges();

    // Assert
    const createButton = fixture.debugElement.nativeElement.querySelector('[data-testid="create-button"]');
    const updateButton = fixture.debugElement.nativeElement.querySelector('[data-testid="update-button"]');
    expect(createButton).toBeNull();
    expect(updateButton).toBeNull();
  });
});
