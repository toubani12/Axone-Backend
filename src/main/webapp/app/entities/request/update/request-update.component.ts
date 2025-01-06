import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';
import { RequestService } from '../service/request.service';
import { IRequest } from '../request.model';
import { RequestFormService, RequestFormGroup } from './request-form.service';

@Component({
  standalone: true,
  selector: 'jhi-request-update',
  templateUrl: './request-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RequestUpdateComponent implements OnInit {
  isSaving = false;
  request: IRequest | null = null;
  requestStatusValues = Object.keys(RequestStatus);

  relatedApplicationsCollection: IApplication[] = [];
  recruitersSharedCollection: IRecruiter[] = [];

  protected requestService = inject(RequestService);
  protected requestFormService = inject(RequestFormService);
  protected applicationService = inject(ApplicationService);
  protected recruiterService = inject(RecruiterService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RequestFormGroup = this.requestFormService.createRequestFormGroup();

  compareApplication = (o1: IApplication | null, o2: IApplication | null): boolean => this.applicationService.compareApplication(o1, o2);

  compareRecruiter = (o1: IRecruiter | null, o2: IRecruiter | null): boolean => this.recruiterService.compareRecruiter(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ request }) => {
      this.request = request;
      if (request) {
        this.updateForm(request);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const request = this.requestFormService.getRequest(this.editForm);
    if (request.id !== null) {
      this.subscribeToSaveResponse(this.requestService.update(request));
    } else {
      this.subscribeToSaveResponse(this.requestService.create(request));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequest>>): void {
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

  protected updateForm(request: IRequest): void {
    this.request = request;
    this.requestFormService.resetForm(this.editForm, request);

    this.relatedApplicationsCollection = this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
      this.relatedApplicationsCollection,
      request.relatedApplication,
    );
    this.recruitersSharedCollection = this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(
      this.recruitersSharedCollection,
      request.recruiter,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationService
      .query({ filter: 'request-is-null' })
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing<IApplication>(applications, this.request?.relatedApplication),
        ),
      )
      .subscribe((applications: IApplication[]) => (this.relatedApplicationsCollection = applications));

    this.recruiterService
      .query()
      .pipe(map((res: HttpResponse<IRecruiter[]>) => res.body ?? []))
      .pipe(
        map((recruiters: IRecruiter[]) =>
          this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(recruiters, this.request?.recruiter),
        ),
      )
      .subscribe((recruiters: IRecruiter[]) => (this.recruitersSharedCollection = recruiters));
  }
}
