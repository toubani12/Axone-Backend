import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAppAccountType } from '../app-account-type.model';

@Component({
  standalone: true,
  selector: 'jhi-app-account-type-detail',
  templateUrl: './app-account-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AppAccountTypeDetailComponent {
  appAccountType = input<IAppAccountType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
