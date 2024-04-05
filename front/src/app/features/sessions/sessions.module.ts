import { NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ReactiveFormsModule } from '@angular/forms';
//import { MatLegacyButtonModule as MatButtonModule } from '@angular/material/legacy-button';
import { MatButtonModule } from '@angular/material/button';
//import { MatLegacyCardModule as MatCardModule } from '@angular/material/legacy-card';
import { MatCardModule } from '@angular/material/card';
//import { MatLegacyFormFieldModule as MatFormFieldModule } from '@angular/material/legacy-form-field';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
//import { MatLegacyInputModule as MatInputModule } from '@angular/material/legacy-input';
import { MatInputModule } from '@angular/material/input';
//import { MatLegacySelectModule as MatSelectModule } from '@angular/material/legacy-select';
import { MatSelectModule } from '@angular/material/select';
//import { MatLegacySnackBarModule as MatSnackBarModule } from '@angular/material/legacy-snack-bar';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ListComponent } from './components/list/list.component';
import { FormComponent } from './components/form/form.component';
import { DetailComponent } from './components/detail/detail.component';
import { SessionsRoutingModule } from './sessions-routing.module';
import localeFr from '@angular/common/locales/fr';
registerLocaleData(localeFr);

const materialModules = [
  MatButtonModule,
  MatCardModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatSnackBarModule,
  MatSelectModule
];

@NgModule({
  declarations: [
    ListComponent,
    FormComponent,
    DetailComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    ReactiveFormsModule,
    SessionsRoutingModule,
    ...materialModules
  ]
})
export class SessionsModule { }
