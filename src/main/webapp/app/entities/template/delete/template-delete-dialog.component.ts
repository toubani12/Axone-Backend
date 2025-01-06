import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITemplate } from '../template.model';
import { TemplateService } from '../service/template.service';

@Component({
  standalone: true,
  templateUrl: './template-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TemplateDeleteDialogComponent {
  template?: ITemplate;

  protected templateService = inject(TemplateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.templateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
