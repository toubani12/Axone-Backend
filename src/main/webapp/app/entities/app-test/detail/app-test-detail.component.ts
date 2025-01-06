import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAppTest } from '../app-test.model';

@Component({
  standalone: true,
  selector: 'jhi-app-test-detail',
  templateUrl: './app-test-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AppTestDetailComponent {
  appTest = input<IAppTest | null>(null);

  previousState(): void {
    window.history.back();
  }
}
