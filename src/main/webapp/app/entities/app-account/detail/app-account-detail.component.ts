import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAppAccount } from '../app-account.model';

@Component({
  standalone: true,
  selector: 'jhi-app-account-detail',
  templateUrl: './app-account-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AppAccountDetailComponent {
  appAccount = input<IAppAccount | null>(null);

  previousState(): void {
    window.history.back();
  }
}
