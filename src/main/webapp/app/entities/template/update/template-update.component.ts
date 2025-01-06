import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { TemplateContractType } from 'app/entities/enumerations/template-contract-type.model';
import { TemplateService } from '../service/template.service';
import { ITemplate } from '../template.model';
import { TemplateFormService, TemplateFormGroup } from './template-form.service';

@Component({
  standalone: true,
  selector: 'jhi-template-update',
  templateUrl: './template-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TemplateUpdateComponent implements OnInit {
  isSaving = false;
  template: ITemplate | null = null;
  templateContractTypeValues = Object.keys(TemplateContractType);

  employersSharedCollection: IEmployer[] = [];
  applicationsSharedCollection: IApplication[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected templateService = inject(TemplateService);
  protected templateFormService = inject(TemplateFormService);
  protected employerService = inject(EmployerService);
  protected applicationService = inject(ApplicationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TemplateFormGroup = this.templateFormService.createTemplateFormGroup();

  compareEmployer = (o1: IEmployer | null, o2: IEmployer | null): boolean => this.employerService.compareEmployer(o1, o2);

  compareApplication = (o1: IApplication | null, o2: IApplication | null): boolean => this.applicationService.compareApplication(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ template }) => {
      this.template = template;
      if (template) {
        this.updateForm(template);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('hrSolutionApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const template = this.templateFormService.getTemplate(this.editForm);
    if (template.id !== null) {
      this.subscribeToSaveResponse(this.templateService.update(template));
    } else {
      this.subscribeToSaveResponse(this.templateService.create(template));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITemplate>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(template: ITemplate): void {
    this.template = template;
    this.templateFormService.resetForm(this.editForm, template);

    this.employersSharedCollection = this.employerService.addEmployerToCollectionIfMissing<IEmployer>(
      this.employersSharedCollection,
      template.owner,
    );
    this.applicationsSharedCollection = this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
      this.applicationsSharedCollection,
      ...(template.applications ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employerService
      .query()
      .pipe(map((res: HttpResponse<IEmployer[]>) => res.body ?? []))
      .pipe(
        map((employers: IEmployer[]) => this.employerService.addEmployerToCollectionIfMissing<IEmployer>(employers, this.template?.owner)),
      )
      .subscribe((employers: IEmployer[]) => (this.employersSharedCollection = employers));

    this.applicationService
      .query()
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing<IApplication>(applications, ...(this.template?.applications ?? [])),
        ),
      )
      .subscribe((applications: IApplication[]) => (this.applicationsSharedCollection = applications));
  }
}
